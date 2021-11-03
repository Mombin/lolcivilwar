package kr.co.mcedu.match.model.request;

import kr.co.mcedu.match.model.CustomMatchResult;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomMatchSaveRequest {
    private Long groupSeq;
    private List<CustomMatchResult> matchResult = new ArrayList<>();
}