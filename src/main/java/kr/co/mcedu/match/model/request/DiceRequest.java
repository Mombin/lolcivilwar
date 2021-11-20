package kr.co.mcedu.match.model.request;

import kr.co.mcedu.match.model.PositionDice;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
public class DiceRequest {
    private List<Position> list = new ArrayList<>();
    private Boolean newTeamFlag = false;

    @Getter
    @Setter
    @ToString
    public static class Position {
        private String position;
        private String name;
        private PositionDice positionDice = new PositionDice();
    }
}