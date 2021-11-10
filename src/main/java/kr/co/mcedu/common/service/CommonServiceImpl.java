package kr.co.mcedu.common.service;

import kr.co.mcedu.common.entity.SystemEntity;
import kr.co.mcedu.common.entity.SystemPropertyKey;
import kr.co.mcedu.common.repository.SystemRepository;
import kr.co.mcedu.riot.ApiEngine;
import kr.co.mcedu.riot.RiotApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    public ApiEngine apiEngine;

    private final SystemRepository systemRepository;

    public CommonServiceImpl(SystemRepository systemRepository) {
        this.systemRepository = systemRepository;
    }

    public RiotApiProperty getRiotApiProperty(){
        Optional<SystemEntity> systemEntity = systemRepository.findById(SystemPropertyKey.RIOT_PROPERTY.name());
        return systemEntity.map(RiotApiProperty::new).orElse(null);

    }

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
