package kr.co.mcedu.group.service.impl;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.entity.GroupAuthEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.group.model.request.GroupExpelRequest;
import kr.co.mcedu.group.model.request.GroupInviteRequest;
import kr.co.mcedu.group.repository.GroupManageRepository;
import kr.co.mcedu.group.service.GroupUserService;
import kr.co.mcedu.user.entity.GroupInviteEntity;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.user.repository.WebUserRepository;
import kr.co.mcedu.user.service.UserAlarmService;
import kr.co.mcedu.user.service.WebUserService;
import kr.co.mcedu.utils.SessionUtils;
import kr.co.mcedu.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupUserServiceImpl
        implements GroupUserService {

    private final GroupManageRepository groupManageRepository;
    private final WebUserRepository webUserRepository;

    private final WebUserService webUserService;
    private final UserAlarmService userAlarmService;

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

    /**
     * 그룹에 초대하기
     * @param request 초대요청
     */
    @Override
    @Transactional
    public void inviteUser(final GroupInviteRequest request) throws ServiceException {
        if (StringUtils.isEmpty(request.getLolcwTag())) {
            throw new ServiceException("잘못된 태그입니다");
        }
        
        GroupAuthEnum groupAuth = SessionUtils.getGroupAuth(request.getGroupSeq());

        if (groupAuth.getOrder() < GroupAuthEnum.MANAGER.getOrder()) {
            throw new AccessDeniedException("권한이 부족합니다.");
        }

        Optional<WebUserEntity> entityOptional = webUserRepository.findWebUserEntityByLolcwTag(request.getLolcwTag());
        WebUserEntity inviteUser = entityOptional.orElseThrow(() -> new DataNotExistException("존재하지 않는 사용자입니다."));
        if (!inviteUser.getUserSeq().equals(request.getUserSeq())) {
            throw new ServiceException("잘못된 요청입니다.");
        }
        WebUserEntity currentUserEntity = webUserService.findWebUserEntity(SessionUtils.getUserSeq());

        GroupEntity groupEntity = groupManageRepository.findByIdFetch(request.getGroupSeq())
                                                       .orElseThrow(DataNotExistException::new);

        GroupInviteEntity groupInviteEntity = new GroupInviteEntity();
        groupInviteEntity.setGroup(groupEntity);
        groupInviteEntity.setInvitedUser(inviteUser);
        groupInviteEntity.setUser(currentUserEntity);
        groupInviteEntity.setExpireResult(false);

        groupInviteEntity = groupManageRepository.save(groupInviteEntity);

        userAlarmService.sendInviteAlarm(inviteUser, groupInviteEntity);
    }
}
