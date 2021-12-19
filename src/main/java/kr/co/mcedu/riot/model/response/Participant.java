package kr.co.mcedu.riot.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Participant {
    private Long teamId;
    private Long spell1Id;
    private Long spell2Id;
    private Long championId;
    private Map<String,Object> perks = new HashMap<>();

}
