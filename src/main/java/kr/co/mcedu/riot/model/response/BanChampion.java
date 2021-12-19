package kr.co.mcedu.riot.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BanChampion {
    private Long championId;
    private Long teamId;
    private Long pickTurn;
}
