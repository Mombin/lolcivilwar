package kr.co.mcedu.group.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonalResultRequest {
    private Long groupSeq;
    private Long customUserSeq;
    private Integer page;
}