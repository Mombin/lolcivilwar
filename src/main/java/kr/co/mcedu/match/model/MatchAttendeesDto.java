package kr.co.mcedu.match.model;

import kr.co.mcedu.group.model.CustomUserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MatchAttendeesDto {
    private Long attendeesSeq;
    private CustomUserDto customUser;
    private String team;
    private String position;
    private boolean matchResult = false;
    private LocalDateTime createdDate = null;
    private CustomMatchDto customMatch;
}
