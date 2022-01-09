package kr.co.mcedu.group.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayChampionCounter {
    private String championName;
    private String championImageUrl;
    private Long championId;
    private Long total = 0L;
    private Long win = 0L;

    public PlayChampionCounter(Long championId) {
        this.championId = championId;
        this.total = 0L;
        this.win = 0L;
    }

    public void win() {
        win++;
        total++;
    }

    public void lose() {
        total++;
    }
    
    public double getRate() {
        if(total != 0L) {
            return  (win / (double) total) * 100.0;
        }
        return 0;
    }
}
