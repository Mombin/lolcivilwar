package kr.co.mcedu.group.service;

import kr.co.mcedu.common.model.PageWrapper;
import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.model.request.GroupExpelRequest;
import kr.co.mcedu.group.model.request.GroupInviteRequest;
import kr.co.mcedu.group.model.request.ReplyInviteRequest;
import kr.co.mcedu.group.model.response.GroupAuthResponse;
import kr.co.mcedu.group.model.response.GroupInviteHistoryResponse;

import java.util.List;

public interface GroupUserService {
    void expelUser(GroupExpelRequest request) throws ServiceException;

    void inviteUser(GroupInviteRequest request) throws ServiceException;

    void replyInviteMessage(ReplyInviteRequest request) throws ServiceException;

    List<GroupAuthResponse> getAuthUserList(Long groupSeq) throws ServiceException;

    PageWrapper<GroupInviteHistoryResponse> getInviteUserHistory(Long groupSeq, final Integer page) throws AccessDeniedException;
}
