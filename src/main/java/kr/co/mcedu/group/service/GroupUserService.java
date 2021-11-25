package kr.co.mcedu.group.service;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.model.request.GroupExpelRequest;

public interface GroupUserService {
    void expelUser(GroupExpelRequest request) throws ServiceException;
}
