package kr.co.mcedu.group.model;

import kr.co.mcedu.group.entity.GroupAuthEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class GroupAuthDto implements Serializable {
    private Long groupSeq;
    private GroupAuthEnum groupAuth;
}
