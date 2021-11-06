package kr.co.mcedu.match.model.response;

import kr.co.mcedu.group.entity.CustomUserEntity;
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
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
public class MatchHistoryResponse implements Serializable {
    private Integer totalPage;
    private List<MatchHistoryElement> list;

    public MatchHistoryResponse() {
        this.totalPage = 0;
        this.list = new ArrayList<>();
    }

    public MatchHistoryResponse setPage(Page<CustomMatchEntity> page) {
        this.totalPage = page.getTotalPages();
        final AtomicLong matchNumber = new AtomicLong(page.getTotalElements() - ((long) page.getNumber() * page.getSize()));
        page.get().forEach(it -> {
            List<String> aList = new ArrayList<>();
            List<String> bList = new ArrayList<>();
            it.getMatchAttendees().forEach(matchAttendeesEntity -> {
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

            MatchHistoryElement matchHistoryElement = new MatchHistoryElement();
            matchHistoryElement.setMatchNumber(matchNumber.getAndDecrement());
            matchHistoryElement.setDate(Optional.ofNullable(it.getCreatedDate())
                                                .map(localDateTime -> localDateTime.format(
                                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                                .orElse(""));
            matchHistoryElement.setAList(aList);
            matchHistoryElement.setBList(bList);
            MatchAttendeesEntity matchAttendeesEntity = it.getMatchAttendees().get(0);
            String winner = matchAttendeesEntity.getTeam();
            if(!matchAttendeesEntity.isMatchResult()) {
                winner = teamFlip(winner);
            }
            matchHistoryElement.setWinner(winner);
            matchHistoryElement.setMatchSeq(Optional.ofNullable(it.getMatchSeq()).orElse(0L));
            list.add(matchHistoryElement);
        });
        return this;
    }

    private String teamFlip(String team) {
        if("A".equals(team)) {
            return "B";
        } else {
            return "A";
        }
    }
    @Getter
    @Setter
    static class MatchHistoryElement implements Serializable {
        private Long matchNumber = 0L;
        private String winner = "";
        private String date = "";
        private List<String> aList = new ArrayList<>();
        private List<String> bList = new ArrayList<>();
        private Long matchSeq = 0L;
    }
}