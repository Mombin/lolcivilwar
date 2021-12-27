package kr.co.mcedu.match.service.impl;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.group.entity.GroupSeasonEntity;
import kr.co.mcedu.group.service.GroupService;
import kr.co.mcedu.match.entity.CustomMatchEntity;
import kr.co.mcedu.match.model.CustomMatchResult;
import kr.co.mcedu.match.model.Position;
import kr.co.mcedu.match.model.request.CustomMatchSaveRequest;
import kr.co.mcedu.match.model.request.DiceRequest;
import kr.co.mcedu.match.model.response.DiceResponse;
import kr.co.mcedu.match.repository.CustomMatchRepository;
import kr.co.mcedu.match.repository.MatchAttendeesRepository;
import kr.co.mcedu.match.service.CustomMatchService;
import kr.co.mcedu.riot.engine.ApiEngine;
import kr.co.mcedu.riot.engine.RiotApiRequest;
import kr.co.mcedu.riot.engine.RiotApiType;
import kr.co.mcedu.riot.engine.response.CurrentGameInfoResponse;
import kr.co.mcedu.riot.engine.response.DefaultApiResponse;
import kr.co.mcedu.riot.engine.response.RiotApiResponse;
import kr.co.mcedu.riot.service.RiotDataService;
import kr.co.mcedu.utils.LocalCacheManager;
import kr.co.mcedu.utils.SessionUtils;
import kr.co.mcedu.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomMatchServiceImpl
        implements CustomMatchService {
    private final GroupService groupService;
    private final RiotDataService riotDataService;

    private final CustomMatchRepository customMatchRepository;
    private final MatchAttendeesRepository matchAttendeesRepository;

    private final ApiEngine apiEngine;
    private final LocalCacheManager cacheManager;


    @Override
    @Transactional
    public void saveCustomMatchResult(CustomMatchSaveRequest customMatchSaveRequest) throws ServiceException {
        SessionUtils.groupAuthorityCheck(customMatchSaveRequest.getGroupSeq(), GroupAuthEnum::isMatchableAuth);
        if (customMatchSaveRequest.getMatchResult().isEmpty()) {
            throw new ServiceException("잘못된 데이터입니다.");
        }

        GroupEntity groupEntity = groupService.getGroup(customMatchSaveRequest.getGroupSeq());
        GroupSeasonEntity groupSeasonEntity = groupService.getGroupSeasonEntity(customMatchSaveRequest.getSeasonSeq());
        CustomMatchEntity entity = new CustomMatchEntity();
        entity.setGroup(groupEntity);
        entity.setGroupSeason(groupSeasonEntity);
        entity = customMatchRepository.save(entity);

        for (CustomMatchResult it : customMatchSaveRequest.getMatchResult()) {
            String searchTarget;
            if (it.getUser().contains("[")) {
                searchTarget = it.getUser().substring(0, it.getUser().indexOf('['));
            } else {
                searchTarget = it.getUser();
            }
            Optional<CustomUserEntity> customUserOpt = groupEntity.getCustomUser().stream()
                                                          .filter(customUser -> searchTarget.equals(
                                                                  customUser.getNickname())).findFirst();
            if (!customUserOpt.isPresent()) {
                continue;
            }

            CustomUserEntity customUserEntity = customUserOpt.get();
            cacheManager.invalidSynergyCache(customUserEntity.getSeq() + "_" + customMatchSaveRequest.getSeasonSeq());
            cacheManager.getPersonalResultHistoryCache().invalidate(String.valueOf(customUserEntity.getSeq()));
            it.setCustomUser(customUserEntity);
            it.setCustomMatch(entity);
            matchAttendeesRepository.save(it.toEntity());
        }
        cacheManager.invalidMatchHistoryCache(groupEntity.getGroupSeq().toString());
    }

    @Override
    public Map<String, DiceResponse> randomDice(DiceRequest request) throws ServiceException {
        Map<String, DiceResponse> positionMap = new HashMap<>();
        Position[] values = Position.values();
        boolean allFlag = true;
        while (allFlag) {
            boolean resetFlag = false;
            for (DiceRequest.Position it : request.getList()) {
                while (true) {
                    Position position = it.getPositionDice().getPosition();
                    String currentPositionName = position.name();

                    if (!request.getNewTeamFlag()) {
                        if (positionMap.size() == values.length - 1) {
                            Optional<Position> notIncludePosition = Arrays.stream(values)
                                                             .filter(enumPosition -> !positionMap.containsKey(
                                                                     enumPosition.name())).findFirst();
                            if (!notIncludePosition.isPresent()) {
                                throw new ServiceException("잘못된 요청입니다.");
                            }
                            String lastPosition = notIncludePosition.get().name();
                            if  (it.getPosition().toUpperCase().equals(lastPosition)) {
                                resetFlag = true;
                                break;
                            }
                        }
                        if (it.getPosition().toUpperCase().equals(currentPositionName)) {
                            continue;
                        }
                    }

                    if (positionMap.containsKey(currentPositionName)) {
                        continue;
                    }
                    positionMap.put(currentPositionName, new DiceResponse(currentPositionName, it.getName()));
                    break;
                }
            }
            if (resetFlag) {
                positionMap.clear();
                continue;
            }

            allFlag = false;
        }
        return positionMap;
    }

    @Override
    public RiotApiResponse getGameInfo(List<String> encryptedSummonerIdList) {
        if (CollectionUtils.isEmpty(encryptedSummonerIdList)) {
            return new DefaultApiResponse();
        }
        Map<Long, CurrentGameInfoResponse> gameInfoMap = new HashMap<>();
        RiotApiResponse resultInfo = null;
        for (String encryptedSummonerId : encryptedSummonerIdList) {
            if (StringUtils.isEmpty(encryptedSummonerId)) {
                continue;
            }
            RiotApiRequest request = new RiotApiRequest();
            request.setApiType(RiotApiType.CURRENT_GAME_INFO_BY_ENCRYPTED_ACCOUNT_ID);
            request.setData(Collections.singletonMap("encryptedSummonerId", encryptedSummonerId));
            RiotApiResponse riotApiResponse = apiEngine.sendRequest(request);
            if (riotApiResponse instanceof CurrentGameInfoResponse) {
                Long gameId = ((CurrentGameInfoResponse) riotApiResponse).getGameId();
                resultInfo = gameInfoMap.get(gameId);
                if (resultInfo != null) {
                    break;
                }
                gameInfoMap.put(gameId, ((CurrentGameInfoResponse) riotApiResponse));
            }
        }
        if(resultInfo == null && gameInfoMap.size() == 1){
            resultInfo = new ArrayList<>(gameInfoMap.values()).get(0);
        }
        if (resultInfo != null) {
            CurrentGameInfoResponse gameInfoResponse = (CurrentGameInfoResponse) resultInfo;
            gameInfoResponse.getParticipants().forEach(participant -> {
                Long championId = participant.getChampionId();
                participant.setChampionName(riotDataService.getChampionName(championId));
                participant.setChampionImage(riotDataService.getChampionImageUrl(participant.getChampionName()));
            });
            gameInfoResponse.getBannedChampions().forEach(banChampion -> {
                Long championId = banChampion.getChampionId();
                banChampion.setChampionName(riotDataService.getChampionName(championId));
                banChampion.setChampionImage(riotDataService.getChampionImageUrl(banChampion.getChampionName()));
            });
        }
        return Optional.ofNullable(resultInfo).orElse(new DefaultApiResponse());
    }
}