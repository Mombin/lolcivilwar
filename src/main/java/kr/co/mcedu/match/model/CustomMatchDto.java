package kr.co.mcedu.match.model;

import kr.co.mcedu.group.model.GroupSeasonDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomMatchDto {
    private Long matchSeq;
    private LocalDateTime createdDate;
    private List<MatchAttendeesDto> matchAttendees = new ArrayList<>();
    private GroupSeasonDto groupSeason;
}
