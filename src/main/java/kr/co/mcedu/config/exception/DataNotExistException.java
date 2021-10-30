package kr.co.mcedu.config.exception;


/**
 * DataNotExistException
 * 데이터가 없을때
 */
public class DataNotExistException extends ServiceException {
    public DataNotExistException() {
        this("데이터가 존재하지 않습니다.");
    }
    public DataNotExistException(String viewMessage) {
        super(viewMessage);
    }
}
