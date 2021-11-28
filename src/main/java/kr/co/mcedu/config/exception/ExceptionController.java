package kr.co.mcedu.config.exception;

import kr.co.mcedu.utils.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Exception handler
 */
@Slf4j
@ControllerAdvice
class ExceptionController {

    /**
     * DataNotExistException handler
     */
    @ExceptionHandler(DataNotExistException.class)
    public ResponseEntity<Object> handleServiceException(DataNotExistException ex) {
        logError(ex.getViewMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseWrapper.fail(ex.getViewMessage()).build());
    }

    /**
     * AccessDeniedException handler
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleServiceException(AccessDeniedException ex) {
        logError(ex.getViewMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseWrapper.fail(ex.getViewMessage()).build());
    }

    /**
     * ServiceException handler
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException ex) {
        logError(ex.getViewMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseWrapper.fail(ex.getViewMessage()).build());
    }

    /**
     * All Exception handler
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex) {
        log.error("error : ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseWrapper.fail().build());
    }

    private void logError(final String viewMessage) {
        log.error("error : {}", viewMessage);
    }
}