package kr.co.mcedu.common.service;

import kr.co.mcedu.riot.RiotApiProperty;

public interface CommonService {
    RiotApiProperty getRiotApiProperty();

    void updateRiotApiProperty(String apiKey);
}