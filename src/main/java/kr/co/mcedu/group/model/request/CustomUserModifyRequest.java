package kr.co.mcedu.group.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomUserModifyRequest {
    private Long groupSeq;
    private String nickname;
    private String summonerId;
    private Long customUserSeq;
}