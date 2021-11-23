package kr.co.mcedu.group.service.impl;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.entity.GroupAuthEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.model.request.GroupExpelRequest;
import kr.co.mcedu.group.repository.GroupManageRepository;
import kr.co.mcedu.group.service.GroupUserService;
import kr.co.mcedu.user.service.WebUserService;
import kr.co.mcedu.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupUserServiceImpl
        implements GroupUserService {

    private final GroupManageRepository groupManageRepository;
    private final WebUserService webUserService;

    /**
     * 그룹에서 추방하기
     * @param request 추방요청
     */
    @Override
    @Transactional
    public void expelUser(final GroupExpelRequest request) throws ServiceException {
        GroupAuthEnum groupAuth = SessionUtils.getGroupAuth(request.getGroupSeq());

        if (groupAuth.getOrder() < GroupAuthEnum.MANAGER.getOrder()) {
            throw new AccessDeniedException("권한이 부족합니다.");
        }

        Optional<GroupAuthEntity> groupOpt = groupManageRepository.getGroupAuthByGroupSeqAndUserSeq(request.getGroupSeq(), request.getUserSeq());
        GroupAuthEntity groupAuthEntity = groupOpt.orElseThrow(DataNotExistException::new);

        if (groupAuth.getOrder() <= groupAuthEntity.getGroupAuth().getOrder()) {
            throw new AccessDeniedException("권한이 부족합니다.");
        }

        groupManageRepository.delete(groupAuthEntity);
        webUserService.pushRefreshedUser(request.getUserSeq());
    }
}
