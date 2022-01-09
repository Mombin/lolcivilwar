package kr.co.mcedu.group.model.response;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Boolean matchResult;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    private Long championId;
    private String championName;
    private String championImageUrl;
}
