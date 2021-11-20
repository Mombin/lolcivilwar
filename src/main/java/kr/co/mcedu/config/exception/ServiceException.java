package kr.co.mcedu.config.exception;

import lombok.Getter;

/**
 * ServiceException
 * 화면에 뿌려줄 exception message를 저장
 */
@Getter
public class ServiceException extends Exception {
    private final String viewMessage;
    public ServiceException(String viewMessage) {
        super(viewMessage);
        this.viewMessage = viewMessage;
    }
}
