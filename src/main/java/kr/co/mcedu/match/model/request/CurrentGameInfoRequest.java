package kr.co.mcedu.match.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CurrentGameInfoRequest {
    private List<String> encryptIdList = new ArrayList<>();
}
