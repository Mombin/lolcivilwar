package kr.co.mcedu.group.model.response;

import kr.co.mcedu.match.entity.CustomMatchEntity;
import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class PersonalResultResponse implements Serializable {
    private Integer totalPage = 0;
    private List<PersonalResultElement> list = new ArrayList<>();

    public PersonalResultResponse setPage(Page<MatchAttendeesEntity> page) {
        this.totalPage = page.getTotalPages();
        list.clear();
        page.get().forEach(it -> {
            PersonalResultElement result = new PersonalResultElement();
            result.setDate(Optional.ofNullable(it.getCreatedDate()).map(localDateTime -> localDateTime.format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).orElse(""));
            result.setSeasonName(it.getCustomMatch().getGroupSeason().getSeasonName());
            result.setPosition(it.getPosition());
            result.setWinYn(it.isMatchResult() ? "Y" : "N");

            Optional<MatchAttendeesEntity> matchUserAttendees = Optional.ofNullable(it.getCustomMatch())
                                                                          .map(CustomMatchEntity::getMatchAttendees)
                                                                          .flatMap(matchAttendeesEntities ->
                                                                                  matchAttendeesEntities.stream()
                                                                                                        .filter(currentMatch -> currentMatch.getPosition().equals(it.getPosition())
                                                                                                                && currentMatch.isMatchResult() != it.isMatchResult()).findFirst());
            matchUserAttendees.ifPresent(matchAttendeesEntity -> {
                String matchUser = Optional.ofNullable(matchAttendeesEntity.getCustomUserEntity())
                                           .map(targetUser -> targetUser.getNickname() + "[" + targetUser.getSummonerName() + "]")
                                           .orElse("삭제유저");
                result.setMatchUser(matchUser);

                Optional.ofNullable(matchAttendeesEntity.getMatchPickChampion()).ifPresent(matchUserChampion -> result.setMatchChampion(matchUserChampion.getPickChampId()));
            });


            Optional.ofNullable(it.getMatchPickChampion()).ifPresent(matchPickChampion -> result.setPickChampion(matchPickChampion.getPickChampId()));
            list.add(result);
        });
        return this;
    }

    @Getter
    @Setter
    public static class PersonalResultElement implements Serializable {
        private String date;
        private String seasonName;
        private String position;
        private String winYn;
        private String matchUser;
        private Long pickChampion;
        private String pickChampionUrl;
        private Long matchChampion;
        private String matchChampionUrl;
    }
}