package kr.co.mcedu.riot;


import kr.co.mcedu.common.entity.SystemEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RiotApiProperty {
    private String riotApiUrl;
    private String apiKey;

    public RiotApiProperty(SystemEntity systemEntity){
        this.riotApiUrl = systemEntity.getPropertyValue1();
        this.apiKey = systemEntity.getPropertyValue2();;
    }

    public RiotApiProperty(){
        SystemEntity systemEntity = new SystemEntity();
        systemEntity.setPropertyValue1("");
        systemEntity.setPropertyValue2("");
    }
}
