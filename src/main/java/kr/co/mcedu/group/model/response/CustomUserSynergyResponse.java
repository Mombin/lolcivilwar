package kr.co.mcedu.group.model.response;

import kr.co.mcedu.group.entity.SynergyModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CustomUserSynergyResponse {
    private Long groupSeq;
    private Long seasonSeq;
    private List<SynergyModel> synergy;
    private List<SynergyModel> badSynergy;
    public CustomUserSynergyResponse() {
        this.groupSeq = 0L;
        this.synergy = new ArrayList<>();
        this.badSynergy = new ArrayList<>();
    }
}