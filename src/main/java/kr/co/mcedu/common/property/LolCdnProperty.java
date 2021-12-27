package kr.co.mcedu.common.property;

import kr.co.mcedu.common.entity.SystemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LolCdnProperty {
    private String version = "";
    private String url = "";

    public LolCdnProperty(SystemEntity systemEntity) {
        this.version = systemEntity.getPropertyValue1();
    }

    public String getCdnUrl() {
        return this.url + "/" + this.version;
    }
}
