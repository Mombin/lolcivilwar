package kr.co.mcedu.match.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomMatchDto {
    private Long matchSeq;
    private List<MatchAttendeesDto> matchAttendees = new ArrayList<>();
}
