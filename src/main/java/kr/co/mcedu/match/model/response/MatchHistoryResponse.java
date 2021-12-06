package kr.co.mcedu.match.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MatchHistoryResponse implements Serializable {
    private Integer totalPage;
    private List<MatchHistoryElement> list;

    public MatchHistoryResponse() {
        this.totalPage = 0;
        this.list = new ArrayList<>();
    }

    public static String teamFlip(String team) {
        if("A".equals(team)) {
            return "B";
        } else {
            return "A";
        }
    }
    @Getter
    @Setter
    public static class MatchHistoryElement implements Serializable {
        private Long matchNumber = 0L;
        private String winner = "";
        private String date = "";
        private String seasonName;
        private List<String> aList = new ArrayList<>();
        private List<String> bList = new ArrayList<>();
        private Long matchSeq = 0L;
    }
}