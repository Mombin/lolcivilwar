package kr.co.mcedu.match.model;

import lombok.Getter;

@Getter
class PositionRate {
    private Position position;
    private int rate = 0;
    public PositionRate(Position position, int rate) {
        this.position = position;
        this.rate = rate;
    }

}