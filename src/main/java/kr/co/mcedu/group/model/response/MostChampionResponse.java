package kr.co.mcedu.group.model.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MostChampionResponse {
    private String championKoreaName;
    private boolean matchResult;
    private LocalDateTime createdDate;
    private Long championId;
}
