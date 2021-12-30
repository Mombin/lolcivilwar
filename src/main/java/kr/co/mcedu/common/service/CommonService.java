package kr.co.mcedu.common.service;

import kr.co.mcedu.common.property.LolCdnProperty;
import kr.co.mcedu.common.property.RiotApiProperty;

public interface CommonService {
    RiotApiProperty getRiotApiProperty();

    LolCdnProperty getLolVersionProperty();

    void updateRiotApiProperty(String apiKey);

    void updateLolVersionProperty(String version);
}
