package kr.co.mcedu.match.model.request;

import kr.co.mcedu.match.model.CustomMatchResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CustomMatchSaveRequest {
    private Long groupSeq;
    private Long seasonSeq;
    private List<CustomMatchResult> matchResult = new ArrayList<>();
}