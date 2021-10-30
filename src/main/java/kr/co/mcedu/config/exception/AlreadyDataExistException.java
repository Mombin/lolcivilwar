package kr.co.mcedu.config.exception;


/**
 * AlreadyDataExistException
 * 이미 데이터가 존재함
 */
public class AlreadyDataExistException extends ServiceException{
    public AlreadyDataExistException(String message) {
        super(message);
    }
}
