package kr.co.mcedu.common.property;


import kr.co.mcedu.common.entity.SystemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RiotApiProperty {
    private String riotApiUrl;
    private String apiKey;

    public RiotApiProperty(SystemEntity systemEntity){
        this.riotApiUrl = systemEntity.getPropertyValue1();
        this.apiKey = systemEntity.getPropertyValue2();
    }
}
