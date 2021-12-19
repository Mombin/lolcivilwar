package kr.co.mcedu.riot;

import kr.co.mcedu.riot.model.response.CurrentGameInfoResponse;
import kr.co.mcedu.riot.model.response.DefaultApiResponse;
import kr.co.mcedu.riot.model.response.SummonerNameSearchResponse;
import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public enum RiotApiType {
    DEFAULT("", DefaultApiResponse.class, "요청없음"),
    RANK_BY_ENCRYPTED_ID("/lol/league/v4/entries/by-summoner/{encryptedSummonerId}", SummonerNameSearchResponse.class, "암호화된 아이디로 랭크 검색"),
    SUMMONER("/lol/summoner/v4/summoners/by-name/{summonerName}", SummonerNameSearchResponse.class, "소환사명 검색"),
    SUMMONER_BY_ENCRYPTED_ACCOUNT_ID("/lol/summoner/v4/summoners/by-account/{encryptedAccountId}", SummonerNameSearchResponse.class, "암호화된 아이디로 소환사명 검색"),
    CURRENT_GAME_INFO_BY_ENCRYPTED_ACCOUNT_ID("/lol/spectator/v4/active-games/by-summoner/{encryptedSummonerId}", CurrentGameInfoResponse.class,"암호화된 아이디로 게임데이터 검색");


    private final String url;
    private final Type responseType;
    private final String msg;

    RiotApiType(String url, Type responseType, String msg) {
        this.url = url;
        this.responseType = responseType;
        this.msg = msg;
    }
}
