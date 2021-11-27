package kr.co.mcedu.group.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GroupResultRequest {
    private Long groupSeq;
    private Long seasonSeq;
    private Long customUserSeq;
    public GroupResultRequest(Long groupSeq, Long seasonSeq) {
        this.groupSeq = groupSeq;
        this.seasonSeq = seasonSeq;
    }
}
