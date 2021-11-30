package kr.co.mcedu.group.service;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.model.request.GroupExpelRequest;
import kr.co.mcedu.group.model.request.GroupInviteRequest;
import kr.co.mcedu.group.model.request.ReplyInviteRequest;

public interface GroupUserService {
    void expelUser(GroupExpelRequest request) throws ServiceException;

    void inviteUser(GroupInviteRequest request) throws ServiceException;

    void replyInviteMessage(ReplyInviteRequest request) throws ServiceException;
}
