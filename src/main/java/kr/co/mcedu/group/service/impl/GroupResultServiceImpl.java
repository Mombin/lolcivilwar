package kr.co.mcedu.group.service.impl;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.group.model.request.GroupResultRequest;
import kr.co.mcedu.group.model.response.CustomUserResponse;
import kr.co.mcedu.group.repository.GroupManageRepository;
import kr.co.mcedu.group.service.GroupResultService;
import kr.co.mcedu.match.entity.CustomMatchEntity;
import kr.co.mcedu.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupResultServiceImpl
        implements GroupResultService {

    private final GroupManageRepository groupManageRepository;

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
        List<CustomUserResponse> customUserResponses = new ArrayList<>(map.values());
        return customUserResponses;
    }
}
