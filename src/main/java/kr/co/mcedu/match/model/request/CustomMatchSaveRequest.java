package kr.co.mcedu.match.model.request;

import kr.co.mcedu.match.model.CustomMatchResult;
import kr.co.mcedu.riot.engine.model.BanChampion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class CustomMatchSaveRequest {
    private Long groupSeq;
    private Long seasonSeq;
    private List<CustomMatchResult> matchResult = new ArrayList<>();
    private Map<String, List<BanChampion>> bannedChampions = new HashMap<>();
}