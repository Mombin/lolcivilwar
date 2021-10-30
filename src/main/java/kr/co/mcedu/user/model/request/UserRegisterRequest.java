package kr.co.mcedu.user.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {
    private String id;
    private String password;
    private String email;
}