package kr.co.mcedu.group.service.impl;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.group.entity.SynergyModel;
import kr.co.mcedu.group.model.request.GroupResultRequest;
import kr.co.mcedu.group.model.request.PersonalResultRequest;
import kr.co.mcedu.group.model.response.CustomUserResponse;
import kr.co.mcedu.group.model.response.CustomUserSynergyResponse;
import kr.co.mcedu.group.model.response.PersonalResultResponse;
import kr.co.mcedu.group.repository.CustomUserRepository;
import kr.co.mcedu.group.repository.GroupManageRepository;
import kr.co.mcedu.group.service.GroupResultService;
import kr.co.mcedu.match.entity.CustomMatchEntity;
import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import kr.co.mcedu.match.model.response.MatchHistoryResponse;
import kr.co.mcedu.match.repository.CustomMatchRepository;
import kr.co.mcedu.match.repository.MatchRepository;
import kr.co.mcedu.utils.LocalCacheManager;
import kr.co.mcedu.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupResultServiceImpl
        implements GroupResultService {

    private final LocalCacheManager cacheManager;
    private final GroupManageRepository groupManageRepository;
    private final CustomUserRepository customUserRepository;
    private final MatchRepository matchRepository;
    private final CustomMatchRepository customMatchRepository;

    @Override
    @Transactional
    public List<CustomUserResponse> getRankData(GroupResultRequest request) throws ServiceException {
        GroupAuthEnum myGroupAuth = SessionUtils.getGroupAuth(request.getGroupSeq());
        if (!GroupAuthEnum.isViewAbleAuth(myGroupAuth)) {
            throw new AccessDeniedException("권한이 부족합니다.");
        }

        return getCustomUserBySeason(request);
    }

    @Override
    @Transactional
    public List<CustomUserResponse> getCustomUserBySeason(final GroupResultRequest request)
            throws DataNotExistException {
        Optional<GroupEntity> groupEntityOpt = groupManageRepository.findByIdFetch(request.getGroupSeq());
        GroupEntity groupEntity = groupEntityOpt.orElseThrow(DataNotExistException::new);
        Map<Long, CustomUserResponse> map = groupEntity.getCustomUser().stream().collect(Collectors.toMap(CustomUserEntity::getSeq, CustomUserEntity::toCustomUserResponse));

        List<CustomMatchEntity> customMatchEntities = groupManageRepository.getCustomMatchByGroupSeqAndSeasonSeq(
                request.getGroupSeq(), request.getSeasonSeq());
        customMatchEntities.forEach(it -> it.getMatchAttendees().forEach(matchAttendees -> {
            Optional.ofNullable(matchAttendees.getCustomUserEntity()).map(CustomUserEntity::getSeq).map(map::get)
                    .ifPresent(target -> {

                        Pair<Integer, Integer> pair = target.getPositionWinRate()
                                                            .getOrDefault(matchAttendees.getPosition(), Pair.of(0, 0));
                        Pair<Integer, Integer> newPair = Pair.of(pair.getFirst() + 1,
                                matchAttendees.isMatchResult() ? pair.getSecond() + 1 : pair.getSecond());
                        target.getPositionWinRate().put(matchAttendees.getPosition(), newPair);

                        target.totalIncrease();
                        if (matchAttendees.isMatchResult()) {
                            target.winIncrease();
                        }
                        LocalDateTime createDateOrNow = Optional.ofNullable(matchAttendees.getCreatedDate())
                                                                .orElseGet(LocalDateTime::now);
                        boolean isBeforeCreateDateOrNow = Optional.ofNullable(target.getLastDate())
                                                                  .map(localDateTime -> localDateTime.isBefore(
                                                                          createDateOrNow)).orElse(true);
                        if (isBeforeCreateDateOrNow) {
                            target.setLastDate(createDateOrNow);
                        }
                    });
        }));
        return new ArrayList<>(map.values());
    }

    @Override
    @Transactional
    public List<CustomUserResponse> getMatchAttendees(final GroupResultRequest request) throws ServiceException {
        GroupAuthEnum myGroupAuth = SessionUtils.getGroupAuth(request.getGroupSeq());
        if (!GroupAuthEnum.isViewAbleAuth(myGroupAuth)) {
            throw new AccessDeniedException("권한이 부족합니다.");
        }
        Optional<GroupEntity> groupEntityOpt = groupManageRepository.findByIdFetch(request.getGroupSeq());
        if (!groupEntityOpt.isPresent()) {
            throw new DataNotExistException();
        }

        return groupManageRepository.getMatchAttendeesByGroupSeqAndSeasonSeq(request.getSeasonSeq());
    }

    /**
     * 시너지/상성 계산
     * @param groupResultRequest 요청 request
     * @return 계산 결과
     * @throws ServiceException
     */
    @Override
    @Transactional
    public CustomUserSynergyResponse calculateSynergy(GroupResultRequest groupResultRequest) throws ServiceException {
        Long customUserSeq = Optional.ofNullable(groupResultRequest.getCustomUserSeq()).orElse(0L);
        Long seasonSeq = groupResultRequest.getSeasonSeq();
        CustomUserSynergyResponse result = cacheManager.getSynergy(customUserSeq + "_" + seasonSeq);
        Long requestGroupSeq = groupResultRequest.getGroupSeq();
        if (result != null && Objects.equals(result.getGroupSeq(), requestGroupSeq)) {
            log.info("GetFrom SynergyCache : {}", groupResultRequest);
            return result;
        }
        Optional<CustomUserEntity> userEntityOpt = customUserRepository.findById(customUserSeq);
        if (!userEntityOpt.isPresent()) {
            throw new DataNotExistException("잘못된 요청입니다.");
        }
        CustomUserEntity entity = userEntityOpt.get();
        Optional<GroupEntity> groupEntity = Optional.ofNullable(entity.getGroup());
        if (!groupEntity.isPresent() || !groupEntity.get().getGroupSeq().equals(requestGroupSeq)) {
            throw new ServiceException("잘못된 요청입니다.");
        }

        List<MatchAttendeesEntity> matchList = matchRepository.findAllByCustomUserEntityWithSeasonSeq(entity, seasonSeq);
        Map<Long, SynergyModel> synergy = new HashMap<>();
        Map<Long, SynergyModel> badSynergy = new HashMap<>();
        // 쿼리 조회를 줄이기 위해 전체한번에 조회
        List<Long> matchSeqs = matchList.stream().map(MatchAttendeesEntity::getCustomMatch)
                                        .map(CustomMatchEntity::getMatchSeq).collect(Collectors.toList());
        Map<Long, List<MatchAttendeesEntity>> matchMap = matchRepository.findAllByCustomMatchs(matchSeqs).stream()
                                                                        .collect(Collectors.groupingBy(it -> it.getCustomMatch().getMatchSeq()));
        matchList.forEach(target -> {
            List<MatchAttendeesEntity> allList = matchMap.getOrDefault(target.getCustomMatch().getMatchSeq(), Collections.emptyList());

            allList.stream()
                   .filter(matchAttendeesEntity -> !matchAttendeesEntity.getAttendeesSeq().equals(target.getAttendeesSeq()))
                   .forEach(matchAttendeesEntity -> {
                       Map<Long, SynergyModel> targetSynergy;
                       if (matchAttendeesEntity.getTeam().equals(target.getTeam())) {
                           targetSynergy = synergy;
                       } else {
                           targetSynergy = badSynergy;
                       }
                       Long userSeq = Optional.ofNullable(matchAttendeesEntity.getCustomUserEntity())
                                              .map(CustomUserEntity::getSeq).orElse(0L);
                       SynergyModel synergyModel = targetSynergy.computeIfAbsent(userSeq, a -> new SynergyModel());
                       synergyModel.add(matchAttendeesEntity);
                       targetSynergy.put(userSeq, synergyModel);
                   });
        });
        result = new CustomUserSynergyResponse();
        result.setGroupSeq(requestGroupSeq);
        result.setSeasonSeq(seasonSeq);
        result.getSynergy().addAll(synergy.values());
        result.getBadSynergy().addAll(badSynergy.values());

        cacheManager.putSynergyCache(customUserSeq + "_" + seasonSeq, result);
        return result;
    }

    @Transactional
    @Override
    public MatchHistoryResponse getMatches(Long groupSeq, Integer pageNum) throws AccessDeniedException {
        SessionUtils.groupAuthorityCheck(groupSeq, GroupAuthEnum::isViewAbleAuth);
        Map<Integer, MatchHistoryResponse> map = cacheManager.getMatchHistory(groupSeq.toString());
        Optional<MatchHistoryResponse> result = Optional.ofNullable(map.get(pageNum));
        if (result.isPresent()) {
            log.info("GetFrom MatchHistoryCache : {} , {}", groupSeq, pageNum);
            return result.get();
        }

        Page<CustomMatchEntity> page = customMatchRepository.findByGroup_GroupSeqOrderByMatchSeqDesc(groupSeq,  PageRequest.of(pageNum, 10));

        MatchHistoryResponse matchHistoryResponse = this.setMatchHistoryResponse(page);

        map.put(pageNum, matchHistoryResponse);
        cacheManager.putMatchHistoryCache(groupSeq.toString(), map);

        return matchHistoryResponse;
    }

    private MatchHistoryResponse setMatchHistoryResponse(Page<CustomMatchEntity> page) {
        MatchHistoryResponse matchHistoryResponse = new MatchHistoryResponse();
        matchHistoryResponse.setTotalPage(page.getTotalPages());
        final AtomicLong matchNumber = new AtomicLong(page.getTotalElements() - ((long) page.getNumber() * page.getSize()));
        List<Long> matchSeqs = page.get().map(CustomMatchEntity::getMatchSeq).collect(Collectors.toList());
        Map<Long, List<MatchAttendeesEntity>> matchAttendeesMap =
                groupManageRepository.getMatchAttendees(matchSeqs)
                                     .stream()
                                     .collect(Collectors.groupingBy(it -> it.getCustomMatch().getMatchSeq()));

        page.get().forEach(it -> {
            List<String> aList = new ArrayList<>();
            List<String> bList = new ArrayList<>();

            List<MatchAttendeesEntity> matchAttendees = matchAttendeesMap.getOrDefault(it.getMatchSeq(), Collections.emptyList());
            matchAttendees.forEach(matchAttendeesEntity -> {
                List<String> currentTeamList;
                if ("A".equals(matchAttendeesEntity.getTeam())) {
                    currentTeamList = aList;
                } else {
                    currentTeamList = bList;
                }
                String nickname = Optional.ofNullable(matchAttendeesEntity.getCustomUserEntity())
                                          .map(CustomUserEntity::getNickname).orElse("");
                currentTeamList.add(nickname);
            });

            MatchHistoryResponse.MatchHistoryElement matchHistoryElement = new MatchHistoryResponse.MatchHistoryElement();
            matchHistoryElement.setMatchNumber(matchNumber.getAndDecrement());
            matchHistoryElement.setDate(Optional.ofNullable(it.getCreatedDate())
                                                .map(localDateTime -> localDateTime.format(
                                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                                .orElse(""));
            matchHistoryElement.setSeasonName(it.getGroupSeason().getSeasonName());
            matchHistoryElement.setAList(aList);
            matchHistoryElement.setBList(bList);
            MatchAttendeesEntity matchAttendeesEntity = it.getMatchAttendees().get(0);
            String winner = matchAttendeesEntity.getTeam();
            if(!matchAttendeesEntity.isMatchResult()) {
                winner = MatchHistoryResponse.teamFlip(winner);
            }
            matchHistoryElement.setWinner(winner);
            matchHistoryElement.setMatchSeq(Optional.ofNullable(it.getMatchSeq()).orElse(0L));
            matchHistoryResponse.getList().add(matchHistoryElement);
        });

        return matchHistoryResponse;
    }

    @Override
    @Transactional
    public PersonalResultResponse getPersonalResult(PersonalResultRequest request) throws Exception {
        SessionUtils.groupAuthorityCheck(request.getGroupSeq(), GroupAuthEnum::isViewAbleAuth);

        if (Objects.isNull(request.getPage())) {
            throw new ServiceException("올바르지 않는 페이지입니다.");
        }

        Map<Integer, PersonalResultResponse> map = cacheManager.getPersonalResultHistory(request.getCustomUserSeq().toString());

        Optional<PersonalResultResponse> result = Optional.ofNullable(map.get(request.getPage()));
        if (result.isPresent()) {
            log.info("GetFrom tPersonalResultHistoryCache : {} , {}", request.getCustomUserSeq(), request.getPage());
            return result.get();
        }
        Optional<CustomUserEntity> customUser = groupManageRepository.customUserFetch(request.getCustomUserSeq());
        if (!customUser.isPresent()) {
            throw new DataNotExistException();
        }
        CustomUserEntity customUserEntity = customUser.get();

        Page<MatchAttendeesEntity> attendeesPage = groupManageRepository.findAllPersonalMatchResult(customUserEntity, PageRequest.of(request.getPage(), 10));
        PersonalResultResponse personalResultResponse = new PersonalResultResponse().setPage(attendeesPage);
        map.put(request.getPage(), personalResultResponse);
        cacheManager.putPersonalResultHistory(request.getCustomUserSeq().toString(), map);
        return personalResultResponse;
    }
}
