package kr.co.mcedu.common.service;

import kr.co.mcedu.common.entity.SystemEntity;
import kr.co.mcedu.common.entity.SystemPropertyKey;
import kr.co.mcedu.common.property.LolCdnProperty;
import kr.co.mcedu.common.repository.SystemRepository;
import kr.co.mcedu.riot.engine.ApiEngine;
import kr.co.mcedu.common.property.RiotApiProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final ApiEngine apiEngine;
    private final SystemRepository systemRepository;

    @Value("${lol.cdn-url}")
    private String lolCdnUrl;

    @PostConstruct
    private void init() {
        apiEngine.init(getRiotApiProperty());
    }

    @Override
    public RiotApiProperty getRiotApiProperty(){
        Optional<SystemEntity> systemEntity = systemRepository.findById(SystemPropertyKey.RIOT_PROPERTY.name());
        return systemEntity.map(RiotApiProperty::new).orElse(null);
    }

    @Override
    public LolCdnProperty getLolVersionProperty() {
        LolCdnProperty lolCdnProperty = systemRepository.findById(SystemPropertyKey.LOL_VERSION.name())
                                                        .map(LolCdnProperty::new).orElse(new LolCdnProperty());
        lolCdnProperty.setUrl(lolCdnUrl);
        return lolCdnProperty;
    }

    @Override
    public void updateRiotApiProperty(String apiKey){
        Optional<SystemEntity> systemEntityOption = systemRepository.findById(SystemPropertyKey.RIOT_PROPERTY.name());
        SystemEntity systemEntity;
        if(!systemEntityOption.isPresent()){
            SystemEntity entity = new SystemEntity();
            entity.setPropertyName(SystemPropertyKey.RIOT_PROPERTY.name());
            entity.setPropertyValue1("https://kr.api.riotgames.com");
            systemEntity = entity;
        }else{
            systemEntity = systemEntityOption.get();
        }
        systemEntity.setPropertyValue2(apiKey);
        systemEntity = systemRepository.save(systemEntity);
        apiEngine.updateApiProperty(new RiotApiProperty(systemEntity));
    }

}
