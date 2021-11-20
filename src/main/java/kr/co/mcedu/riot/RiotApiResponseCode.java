package kr.co.mcedu.riot;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum RiotApiResponseCode {
    DEFAULT("000"),
    SUCCESS("200"),
    PARSING_ERROR("997"),
    TIMEOUT("998"),
    PROPERTY_ERROR("999");

    private final String state ;

    RiotApiResponseCode(String state) {
        this.state = state;
    }

    private final static Map<String,RiotApiResponseCode> states = Arrays.stream(values()).collect(Collectors.toMap(RiotApiResponseCode::getState,Function.identity()));
    public static RiotApiResponseCode findByState(String state){
        return states.getOrDefault(state,DEFAULT);
    }
}
