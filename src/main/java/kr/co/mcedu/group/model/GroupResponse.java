package kr.co.mcedu.group.model;

import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.group.entity.GroupSeasonEntity;
import kr.co.mcedu.group.model.response.CustomUserResponse;
import kr.co.mcedu.group.model.response.GroupSeasonResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class GroupResponse {

    private long groupSeq;

    private String groupName;

    private String owner;

    private List<CustomUserResponse> customUser = new ArrayList<>();

    private GroupAuthEnum auth;

    private GroupSeasonResponse defaultSeason;
    private List<GroupSeasonResponse> seasons = new ArrayList<>();

    public GroupResponse setEntity(GroupEntity groupEntity) {
        this.groupSeq = groupEntity.getGroupSeq();
        this.groupName = groupEntity.getGroupName();
        this.owner = groupEntity.getOwner();
        this.customUser = new ArrayList<>();
        this.auth = null;

        Map<Long, CustomUserResponse> map = groupEntity.getCustomUser().stream().collect(
                Collectors.toMap(CustomUserEntity::getSeq, CustomUserEntity::toCustomUserResponse));

        groupEntity.getCustomMatches().stream()
                   .filter(customMatchEntity -> Objects.equals(customMatchEntity.getGroupSeason().getGroupSeasonSeq(), defaultSeason.getSeasonSeq()))
                   .forEach(it -> it.getMatchAttendees().forEach(matchAttendees -> {
                       Optional.ofNullable(matchAttendees.getCustomUserEntity()).map(CustomUserEntity::getSeq).map(map::get).ifPresent(target -> {

                           Pair<Integer, Integer> pair = target.getPositionWinRate()
                                                                    .getOrDefault(matchAttendees.getPosition(), Pair.of(0, 0));
                           Pair<Integer, Integer> newPair = Pair.of(pair.getFirst() + 1,
                                   matchAttendees.isMatchResult() ? pair.getSecond() + 1 : pair.getSecond());
                           target.getPositionWinRate().put(matchAttendees.getPosition(), newPair);

                           target.totalIncrease();
                           if(matchAttendees.isMatchResult()) {
                               target.winIncrease();
                           }
                           LocalDateTime createDateOrNow = Optional.ofNullable(matchAttendees.getCreatedDate())
                                                            .orElseGet(LocalDateTime::now);
                           boolean isBeforeCreateDateOrNow = Optional.ofNullable(target.getLastDate())
                                                                     .map(localDateTime -> localDateTime.isBefore(createDateOrNow))
                                                                     .orElse(true);
                           if (isBeforeCreateDateOrNow) {
                               target.setLastDate(createDateOrNow);
                           }
                       });
                   }));

        this.customUser.addAll(map.values());
        return this;
    }

    public void setSeasonEntity(final GroupSeasonEntity groupSeasonEntity) {
        GroupSeasonResponse groupSeasonResponse = new GroupSeasonResponse();
        groupSeasonResponse.setSeasonSeq(groupSeasonEntity.getGroupSeasonSeq());
        groupSeasonResponse.setSeasonName(groupSeasonEntity.getSeasonName());
        this.defaultSeason = groupSeasonResponse;
    }

    public void setGroupEntity(final GroupEntity groupEntity) {
        this.groupSeq = groupEntity.getGroupSeq();
        this.groupName = groupEntity.getGroupName();
        this.owner = groupEntity.getOwner();
    }
}
