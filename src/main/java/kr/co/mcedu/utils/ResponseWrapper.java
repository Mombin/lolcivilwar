package kr.co.mcedu.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * API 결과 Wrapper
 */
@NoArgsConstructor
public class ResponseWrapper {
    private ResponseResult responseResult = ResponseResult.SUCCESS;
    private Object data = "";
    private String message = "";

    public ResponseWrapper(ResponseResult responseResult) {
        this(responseResult, "", "");
    }
    public ResponseWrapper(ResponseResult responseResult, Object data, String message) {
        this.responseResult = responseResult;
        this.data = data;
        this.message = message;
    }

    public ResponseWrapper setData(Object data) {
        this.data = data;
        return this;
    }

    public static ResponseWrapper fail() {
        return fail("실패하였습니다. 관리자에게 문의해주세요.");
    }

    public static ResponseWrapper fail(String message) {
        return new ResponseWrapper(ResponseResult.FAIL, "", message);
    }

    public Map<String, Object> build() {
        Map<String, Object> result = new HashMap<>();
        result.put("code", responseResult.code);
        result.put("data", data);
        result.put("message", message);
        return result;
    }

    @Getter
    enum ResponseResult {
        SUCCESS("00000"),
        FAIL("99999");
        private final String code;
        ResponseResult(String code) {
            this.code = code;
        }
    }
}