package kr.co.mcedu.riot;

import lombok.Getter;

import java.lang.reflect.Type;

@Getter
public enum RiotApiType {
    DEFAULT,
    SUMMONER,
    SUMMONER_BY_ENCRYPTED_ACCOUNT_ID,
    RANK_BY_ENCRYPTED_ID;

    private  String url;

    private  Type responseType;

    private  String msg;
}
