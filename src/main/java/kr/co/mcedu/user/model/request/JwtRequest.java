package kr.co.mcedu.user.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest {
    private String id;
    private String password;
}