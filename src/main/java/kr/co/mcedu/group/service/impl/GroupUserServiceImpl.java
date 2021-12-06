package kr.co.mcedu.group.service.impl;

import com.querydsl.core.QueryResults;
import kr.co.mcedu.common.model.PageRequest;
import kr.co.mcedu.common.model.PageWrapper;
import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.entity.GroupAuthEntity;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.group.model.request.GroupExpelRequest;
import kr.co.mcedu.group.model.request.GroupInviteRequest;
import kr.co.mcedu.group.model.request.ReplyInviteRequest;
import kr.co.mcedu.group.model.response.GroupAuthResponse;
import kr.co.mcedu.group.model.response.GroupInviteHistoryResponse;
import kr.co.mcedu.group.repository.GroupManageRepository;
import kr.co.mcedu.group.service.GroupUserService;
import kr.co.mcedu.user.entity.GroupInviteEntity;
import kr.co.mcedu.user.entity.UserAlarmEntity;
import kr.co.mcedu.user.entity.WebUserEntity;
import kr.co.mcedu.user.model.UserAlarmType;
import kr.co.mcedu.user.repository.UserAlarmRepository;
import kr.co.mcedu.user.repository.WebUserRepository;
import kr.co.mcedu.user.service.UserAlarmService;
import kr.co.mcedu.user.service.WebUserService;
import kr.co.mcedu.utils.SessionUtils;
import kr.co.mcedu.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupUserServiceImpl
        implements GroupUserService {

    private final GroupManageRepository groupManageRepository;
    private final WebUserRepository webUserRepository;
    private final UserAlarmRepository userAlarmRepository;

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

    /**
     * 초대에 응답하기
     * @param request
     * @throws ServiceException
     */
    @Override
    @Transactional
    public void replyInviteMessage(ReplyInviteRequest request) throws ServiceException{
        UserAlarmEntity userAlarmEntity = userAlarmRepository.findById(Optional.ofNullable(request.getAlarmSeq()).orElse(0L))
                                                             .orElseThrow(DataNotExistException::new);
        if (!userAlarmEntity.getWebUserEntity().getUserSeq().equals(SessionUtils.getUserSeq())) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        GroupInviteEntity groupInviteEntity = userAlarmEntity.getGroupInviteEntity();
        if (userAlarmEntity.getAlarmType() != UserAlarmType.INVITE || groupInviteEntity == null || !request.isValidRequest()) {
            throw new AccessDeniedException();
        }

        userAlarmEntity.setIsRead(true);
        if (Boolean.TRUE.equals(groupInviteEntity.getExpireResult())) {
            throw new AccessDeniedException("이미 응답한 초대입니다.");
        }

        groupInviteEntity.setInviteResult("Y".equals(request.getResult()));
        groupInviteEntity.setExpireResult(true);

        modifyUserGroupAuth(groupInviteEntity.getGroup(), userAlarmEntity.getWebUserEntity(), GroupAuthEnum.USER);
        SessionUtils.refreshAccessToken();
    }

    private void modifyUserGroupAuth(GroupEntity group, WebUserEntity webUserEntity, GroupAuthEnum auth) {
        Optional<GroupAuthEntity> groupAuthEntityOpt = groupManageRepository.getGroupAuthByGroupSeqAndUserSeq(group.getGroupSeq(), webUserEntity.getUserSeq());
        GroupAuthEntity groupAuthEntity = groupAuthEntityOpt.orElseGet(GroupAuthEntity::new);
        groupAuthEntity.setGroup(group);
        groupAuthEntity.setWebUser(webUserEntity);
        groupAuthEntity.setGroupAuth(auth);
        groupManageRepository.save(groupAuthEntity);
    }

    /**
     * 그룹권한 리스트 가져오기 
     * @param groupSeq 그룹순번
     * @return 그룹권한 리스트
     */
    @Override
    @Transactional
    public List<GroupAuthResponse> getAuthUserList(Long groupSeq) throws ServiceException {
        SessionUtils.groupManageableAuthCheck(groupSeq);
        List<GroupAuthEntity> groupAuthEntities = groupManageRepository.getGroupAuthByGroupSeq(groupSeq);
        return groupAuthEntities.stream().map(GroupAuthResponse::new)
                                .sorted(Comparator.comparing(o -> ((GroupAuthResponse) o).getGroupAuth().getOrder()).reversed())
                                .collect(Collectors.toList());
    }

    /**
     * 초대이력 리스트 가져오기
     * @param groupSeq 그룹순번
     * @param page 페이지 0 ->
     * @return 초대이력
     */
    @Override
    @Transactional
    public PageWrapper<GroupInviteHistoryResponse> getInviteUserHistory(final Long groupSeq, Integer page) throws AccessDeniedException {
        SessionUtils.groupManageableAuthCheck(groupSeq);
        if (page == null) {
            page = 0;
        }
        PageRequest pageRequest = new PageRequest(page, 10);
        QueryResults<GroupInviteEntity> groupInviteHistory = groupManageRepository.getGroupInviteHistory(groupSeq, pageRequest);
        PageWrapper<GroupInviteEntity> result = PageWrapper.of(groupInviteHistory);
        return result.change(GroupInviteHistoryResponse::new);
    }
}
