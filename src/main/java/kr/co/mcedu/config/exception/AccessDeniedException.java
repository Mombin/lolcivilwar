package kr.co.mcedu.config.exception;

/**
 * AccessDeniedException
 * 권한부족
 */
public class AccessDeniedException extends ServiceException {
    public AccessDeniedException() {
        this("잘못된 접근입니다.");
    }
    public AccessDeniedException(String message) {
        super(message);
    }
}
