package kr.co.mcedu.group.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomUserSynergyRequest {
    private Long groupSeq;
    private Long customUserSeq;
}