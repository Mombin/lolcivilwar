package kr.co.mcedu.riot.engine.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Rune {
    private List<Long> perkIds;
    private Long perkStyle;
    private Long pertSubStyle;
}
