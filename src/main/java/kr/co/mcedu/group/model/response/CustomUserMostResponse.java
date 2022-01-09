package kr.co.mcedu.group.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomUserMostResponse {

    private List<PlayChampionCounter> mostChampionList;
    private List<PlayChampionCounter> highWinRateChampion;
    private List<MostChampionResponse> recentlyPlayedChampion;

    public CustomUserMostResponse(List<PlayChampionCounter> mostChampionList,List<PlayChampionCounter> highWinRateChampion , List<MostChampionResponse> recentlyPlayedChampion) {
        this.mostChampionList = mostChampionList;
        this.highWinRateChampion = highWinRateChampion;
        this.recentlyPlayedChampion = recentlyPlayedChampion;
    }

}