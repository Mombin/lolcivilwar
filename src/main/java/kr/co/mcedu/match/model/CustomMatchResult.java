package kr.co.mcedu.match.model;

import kr.co.mcedu.group.entity.CustomUserEntity;
import kr.co.mcedu.match.entity.CustomMatchEntity;
import kr.co.mcedu.match.entity.MatchAttendeesEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomMatchResult {
    private String user;
    private String team;
    private Boolean result;
    private String position;
    private CustomUserEntity customUser;
    private CustomMatchEntity customMatch;

    public MatchAttendeesEntity toEntity() {
        return new MatchAttendeesEntity(this);
    }
}