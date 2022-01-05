package kr.co.mcedu.group.model.response;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MostChampionResponse {
    private String championName;
    private String matchResult;
    private LocalDateTime createdDate;
}
