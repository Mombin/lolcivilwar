package kr.co.mcedu.match.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MatchAttendeesDto {
    private Long attendeesSeq;
    private Long customUserSeq;
    private String position;
    private boolean matchResult = false;
    private LocalDateTime createdDate = null;
}
