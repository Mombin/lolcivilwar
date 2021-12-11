package kr.co.mcedu.user.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel(value = "UserInfo")
public class UserInfo implements Serializable {
    @ApiModelProperty(value = "LOLCW TAG", example = "AY305", dataType = "String")
    private String lolcwTag;
    @ApiModelProperty(value = "유저순번", example = "12", dataType = "Long")
    private Long userSeq;
    @ApiModelProperty(value = "유저아이디", example = "wunjo", dataType = "String")
    private String userId;
}
