package kr.co.mcedu.group.service.impl;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.AlreadyDataExistException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.entity.*;
import kr.co.mcedu.group.model.GroupAuthDto;
import kr.co.mcedu.group.model.GroupResponse;
import kr.co.mcedu.group.model.GroupSaveRequest;
import kr.co.mcedu.group.model.request.*;
import kr.co.mcedu.group.model.response.CustomUserSynergyResponse;
import kr.co.mcedu.group.model.response.GroupAuthResponse;
import kr.co.mcedu.group.model.response.PersonalResultResponse;
import kr.co.mcedu.group.repository.CustomUserRepository;
import kr.co.mcedu.group.repository.GroupAuthRepository;
import kr.co.mcedu.group.repository.GroupManageRepository;
import kr.co.mcedu.group.repository.GroupRepository;
import kr.co.mcedu.group.service.GroupService;
import kr.co.mcedu.match.entity.CustomMatchEntity;
import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import kr.co.mcedu.match.model.response.MatchHistoryResponse;
import kr.co.mcedu.match.repository.CustomMatchRepository;
import kr.co.mcedu.match.repository.MatchAttendeesRepository;
import kr.co.mcedu.match.repository.MatchRepository;
import kr.co.mcedu.summoner.entity.SummonerEntity;
import kr.co.mcedu.summoner.service.SummonerService;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.user.service.WebUserService;
import kr.co.mcedu.utils.LocalCacheManager;
import kr.co.mcedu.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final MatchAttendeesRepository matchAttendeesRepository;
    private final CustomUserRepository customUserRepository;
    private final CustomMatchRepository customMatchRepository;
    private final LocalCacheManager cacheManager;
    private final GroupAuthRepository groupAuthRepository;
    private final WebUserService webUserService;
    private final SummonerService summonerService;
    private final GroupManageRepository groupManageRepository;
    private final MatchRepository matchRepository;

    /**
     * groupSeq를 이용하여 해당 GroupEntity 가져옴
     */
    @Override
    public GroupEntity getGroup(Long groupSeq) throws ServiceException {
        GroupAuthEnum groupAuth = SessionUtils.getGroupAuth(groupSeq);
        if (groupAuth == GroupAuthEnum.NONE) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        Optional<GroupEntity> group = groupManageRepository.findByIdFetch(groupSeq);
        if (!group.isPresent()) {
            throw new DataNotExistException("없는 그룹입니다.");
        }
        return group.get();
    }

    /**
     * 그룹 생성
     */
    @Transactional
    @Override
    public Long makeGroup(GroupSaveRequest groupSaveRequest) throws ServiceException {
        String userId = SessionUtils.getId();
        WebUserEntity webUserEntity = webUserService.findWebUserEntityByUserId(userId);

        List<GroupAuthEntity> ownerList = groupAuthRepository.findAllByWebUserAndGroupAuth(webUserEntity.getUserSeq(),
                GroupAuthEnum.OWNER);
        if (!ownerList.isEmpty()) {
            throw new AlreadyDataExistException("이미 데이터가 존재합니다.");
        }

        // TODO: 기존로직 대응 추후 삭제 필요
        GroupAuthEntity groupAuth = new GroupAuthEntity();
        groupAuth.setWebUser(webUserEntity);
        groupAuth.setGroupAuth(GroupAuthEnum.OWNER);
        GroupEntity groupEntity = groupSaveRequest.toEntity();
        groupEntity.addGroupAuth(groupAuth);
        groupEntity.setOwner(userId);
        GroupEntity result = groupRepository.save(groupEntity);
        log.info(result.toString());
        webUserService.pushRefreshedUser(webUserEntity.getUserSeq());
        return result.getGroupSeq();
    }

    /**
     * 내가 속한 그룹 가져오기
     */
    @Transactional
    @Override
    public List<GroupResponse> findMyGroups() {
        Map<Long, GroupAuthDto> groupAuth = SessionUtils.getGroupAuth();
        List<GroupEntity> groupEntities = groupManageRepository.getGroupEntities(groupAuth.keySet());
        ArrayList<GroupResponse> list = new ArrayList<>();
        groupEntities.forEach(groupEntity -> {
            GroupResponse groupResponse = groupEntity.toGroupResponse();
            groupResponse.setAuth(groupAuth.get(groupEntity.getGroupSeq()).getGroupAuth());
            list.add(groupResponse);
        });
        list.forEach(groupResponse -> groupResponse.getCustomUser().parallelStream().forEach(customUserResponse -> {
            if (!customUserResponse.isRefreshTarget()) {
                return;
            }
            Optional.ofNullable(summonerService.getSummoner("", customUserResponse.getAccountId()))
                    .ifPresent(it -> {
                        customUserResponse.setProfileIconId(it.getProfileIconId());
                        customUserResponse.setSummonerLevel(it.getSummonerLevel());
                        customUserResponse.setSummonerName(it.getName());
                    });
        }));
        return list;
    }

    /**
     * 그룹에 새로운 내전인원 등록
     * @param customUserSaveRequest 내전등록 Request
     */
    @Transactional
    @Override
    public void addSummonerInGroup(CustomUserSaveRequest customUserSaveRequest) throws ServiceException {
        SessionUtils.groupManageableAuthCheck(customUserSaveRequest.getGroupSeq());

        GroupEntity group = this.getGroup(customUserSaveRequest.getGroupSeq());
        if (group.getCustomUser().stream().anyMatch(it -> it.getNickname().equals(customUserSaveRequest.getNickname()))){
            throw new AlreadyDataExistException("이미 저장된 닉네임입니다.");
        }

        CustomUserEntity customUser = customUserSaveRequest.toEntity();
        customUser.setGroup(group);
        group.addCustomUser(customUser);
        groupRepository.save(group);
    }

    /**
     * 내전그룹원 삭제 요청
     * @param customUserDeleteRequest 삭제 요청 Request
     */
    @Transactional
    @Override
    public void deleteSummonerInGroup(CustomUserDeleteRequest customUserDeleteRequest) throws ServiceException {
        SessionUtils.groupManageableAuthCheck(customUserDeleteRequest.getGroupSeq());

        GroupEntity groupEntity = this.getGroup(customUserDeleteRequest.getGroupSeq());
        List<CustomUserEntity> list = groupEntity.getCustomUser().stream()
                                                 .filter(it -> customUserDeleteRequest.getUserSeqArray()
                                                                                      .contains(it.getSeq()))
                                                 .collect(Collectors.toList());
        list.forEach(it -> {
            matchAttendeesRepository.deleteAllByCustomUserEntity(it);
            groupEntity.removeCustomUser(it);
        });
        groupRepository.save(groupEntity);
    }

    @Transactional
    @Override
    public void modifySummonerInGroup(CustomUserModifyRequest customUserModifyRequest) throws ServiceException {
        SessionUtils.groupManageableAuthCheck(customUserModifyRequest.getGroupSeq());

        GroupEntity groupEntity = this.getGroup(customUserModifyRequest.getGroupSeq());
        if (groupEntity.getCustomUser().stream().anyMatch(it -> it.getNickname().equals(customUserModifyRequest.getNickname())
                && it.getSeq() != customUserModifyRequest.getCustomUserSeq()
        )) {
            throw new AlreadyDataExistException("이미 있는 닉네임입니다.");
        }

        groupEntity.getCustomUser().forEach(it -> {
            if (it.getSeq() != customUserModifyRequest.getCustomUserSeq()) {
                return;
            }

            if (!it.getSummonerName().equals(customUserModifyRequest.getSummonerId())) {
                it.setSummonerEntity(null);
            }
            it.setNickname(customUserModifyRequest.getNickname());
            it.setSummonerName(customUserModifyRequest.getSummonerId());
        });

        groupRepository.save(groupEntity);
    }

    /**
     * 시너지/상성 계산
     * @param customUserSynergyRequest 요청 request
     * @return 계산 결과
     * @throws ServiceException
     */
    @Override
    @Transactional
    public CustomUserSynergyResponse calculateSynergy(CustomUserSynergyRequest customUserSynergyRequest)
            throws ServiceException {
        Long customUserSeq = Optional.ofNullable(customUserSynergyRequest.getCustomUserSeq()).orElse(0L);
        CustomUserSynergyResponse result = cacheManager.getSynergyCache().getIfPresent(customUserSeq.toString());
        Long requestGroupSeq = customUserSynergyRequest.getGroupSeq();
        if (result != null && Objects.equals(result.getGroupSeq(), requestGroupSeq)) {
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

        List<MatchAttendeesEntity> matchList = matchRepository.findAllByCustomUserEntity(entity);
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
        result.getSynergy().addAll(synergy.values());
        result.getBadSynergy().addAll(badSynergy.values());

        cacheManager.getSynergyCache().put(customUserSeq.toString(), result);
        return result;
    }

    @Transactional
    @Override
    public MatchHistoryResponse getMatches(Long groupSeq, Integer pageNum) throws Exception {
        GroupEntity group = this.getGroup(groupSeq);
        Map<Integer, MatchHistoryResponse> map = cacheManager.getMatchHistoryCache()
                                                                 .get(groupSeq.toString(), HashMap::new);
        Optional<MatchHistoryResponse> result = Optional.ofNullable(map.get(pageNum));
        if (result.isPresent()) {
            return result.get();
        }

        Page<CustomMatchEntity> page = customMatchRepository.findByGroupOrderByMatchSeqDesc(group,  PageRequest.of(pageNum, 10));

        MatchHistoryResponse matchHistoryResponse = new MatchHistoryResponse().setPage(page);
        map.put(pageNum, matchHistoryResponse);
        cacheManager.getMatchHistoryCache().put(groupSeq.toString(), map);
        return matchHistoryResponse;
    }

    /**
     * 매치 삭제
     * @param matchSeq 매치 번호
     * @return result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object deleteMatch(Long matchSeq) throws ServiceException {
        Optional<CustomMatchEntity> match = customMatchRepository.findById(matchSeq);
        if (!match.isPresent()) {
            throw new ServiceException("삭제 할수 없는 매치입니다.");
        }
        CustomMatchEntity matchEntity = match.get();
        SessionUtils.groupManageableAuthCheck(matchEntity.getGroup().getGroupSeq());

        matchEntity.getMatchAttendees().forEach(it -> cacheManager.getSynergyCache().invalidate(
                (Optional.ofNullable(it.getCustomUserEntity()).map(CustomUserEntity::getSeq).orElse(0L)).toString()));
        customMatchRepository.delete(matchEntity);
        cacheManager.getMatchHistoryCache().invalidate(
                (Optional.ofNullable(matchEntity.getGroup()).map(GroupEntity::getGroupSeq).orElse(0L)).toString());
        matchEntity.getMatchAttendees().forEach(it -> cacheManager.getPersonalResultHistoryCache().invalidate(
                Optional.ofNullable(it.getCustomUserEntity()).map(CustomUserEntity::getSeq).orElse(0L).toString()));

        return "success";
    }

    @Transactional
    @Override
    public List<GroupAuthResponse> getAuthUserList(Long groupSeq) throws ServiceException {
        SessionUtils.groupManageableAuthCheck(groupSeq);
        GroupEntity groupEntity = this.getGroup(groupSeq);
        return GroupAuthResponse.of(groupEntity.getGroupAuthList()).stream()
                                .sorted(Comparator.comparingInt(o -> o.getGroupAuth().ordinal()))
                                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void linkSummoner(LinkSummonerRequest request) throws DataNotExistException {
        Optional<CustomUserEntity> customUser = customUserRepository.findById(request.getUserSeq());
        if (!customUser.isPresent() || !customUser.map(CustomUserEntity::getGroup).isPresent()) {
            throw new DataNotExistException();
        }
        CustomUserEntity customUserEntity = customUser.get();
        GroupEntity groupEntity = customUserEntity.getGroup();
        if (!groupEntity.getGroupSeq().equals(request.getGroupSeq())) {
            throw new DataNotExistException();
        }
        SummonerEntity summoner = summonerService.findByAccountId(request.getAccountId());
        customUserEntity.setSummonerEntity(summoner);
        customUserRepository.save(customUserEntity);
    }

    @Override
    @Transactional
    public PersonalResultResponse getPersonalResult(PersonalResultRequest request) throws Exception {
        GroupEntity group = this.getGroup(request.getGroupSeq());
        Optional<CustomUserEntity> customUser = groupManageRepository.customUserFetch(request.getCustomUserSeq());
        if (!customUser.isPresent()) {
            throw new DataNotExistException();
        }
        CustomUserEntity customUserEntity = customUser.get();
        if (!Objects.equals(customUserEntity.getGroup().getGroupSeq(), group.getGroupSeq())){
            throw new DataNotExistException();
        }
        if (Objects.isNull(request.getPage())) {
            throw new ServiceException("올바르지 않는 페이지입니다.");
        }

        HashMap<Integer, PersonalResultResponse> map = cacheManager.getPersonalResultHistoryCache().get(request.getCustomUserSeq().toString(), HashMap::new);
        Optional<PersonalResultResponse> result = Optional.ofNullable(map.get(request.getPage()));
        if (result.isPresent()) {
            return result.get();
        }
        Page<MatchAttendeesEntity> attendeesPage = groupManageRepository.findAllPersonalMatchResult(
                customUserEntity, PageRequest.of(request.getPage(), 10));
        PersonalResultResponse personalResultResponse = new PersonalResultResponse().setPage(attendeesPage);
        map.put(request.getPage(), personalResultResponse);
        cacheManager.getPersonalResultHistoryCache().put(request.getCustomUserSeq().toString(), map);
        return personalResultResponse;
    }

    @Override
    @Transactional
    public void saveTierPoint(final List<SaveTierPointRequest> request) throws AccessDeniedException {
        if (CollectionUtils.isEmpty(request)) {
            return;
        }

        SessionUtils.groupManageableAuthCheck(request.get(0).getGroupSeq());

        List<Long> userSeqs = request.stream().map(SaveTierPointRequest::getUserSeq).collect(Collectors.toList());

        List<CustomUserEntity> customUserEntities = customUserRepository.findAllById(userSeqs);
        if (customUserEntities.isEmpty()) {
            return;
        }

        Map<Long, CustomUserEntity> customUserEntityMap = customUserEntities.stream().collect(
                Collectors.toMap(CustomUserEntity::getSeq, it -> it));

        request.forEach(it -> {
            CustomUserEntity customUserEntity = customUserEntityMap.get(it.getUserSeq());
            if (customUserEntity == null || !Objects.equals(customUserEntity.getGroup().getGroupSeq(), it.getGroupSeq())) {
                return;
            }
            customUserEntity.setTierPoint(Optional.ofNullable(it.getTierPoint()).orElse(0L));
        });
    }
}