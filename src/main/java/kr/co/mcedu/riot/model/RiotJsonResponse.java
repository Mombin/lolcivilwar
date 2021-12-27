package kr.co.mcedu.riot.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RiotJsonResponse {
    private String type;
    private String version;
    private String format;
    private Map<String, Map<String, Object>> data = new HashMap<>();
}
