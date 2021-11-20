package kr.co.mcedu.group.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveTierPointRequest {
    private Long groupSeq;
    private Long userSeq;
    private Long tierPoint;
}