package kr.co.mcedu.group.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomUserDeleteRequest {
    private Long groupSeq;
    private List<Long> userSeqArray;
}