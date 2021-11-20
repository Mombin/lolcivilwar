package kr.co.mcedu.match.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiceResponse {
    private String position;
    private String name;

    public DiceResponse(String position, String name) {
        this.position = position;
        this.name = name;
    }
}