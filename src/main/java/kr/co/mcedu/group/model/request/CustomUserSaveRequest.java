package kr.co.mcedu.group.model.request;

import kr.co.mcedu.group.entity.CustomUserEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomUserSaveRequest {
    private Long groupSeq;
    private String nickname;
    private String summonerId;

    public CustomUserEntity toEntity() {
        return new CustomUserEntity(nickname, summonerId);
    }
}