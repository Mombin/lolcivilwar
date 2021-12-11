package kr.co.mcedu.group.service;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.group.entity.GroupSeasonEntity;
import kr.co.mcedu.group.model.GroupResponse;
import kr.co.mcedu.group.model.GroupSaveRequest;
import kr.co.mcedu.group.model.request.*;

import java.util.List;

public interface GroupService {
    GroupEntity getGroup(Long groupSeq) throws ServiceException;

    Long makeGroup(GroupSaveRequest groupSaveRequest) throws ServiceException;

    List<GroupResponse> findMyGroupsWithDefaultSeason() throws AccessDeniedException;

    void addSummonerInGroup(CustomUserSaveRequest customUserSaveRequest) throws ServiceException;

    void deleteSummonerInGroup(CustomUserDeleteRequest customUserDeleteRequest) throws ServiceException;

    void modifySummonerInGroup(CustomUserModifyRequest customUserModifyRequest) throws ServiceException;

    Object deleteMatch(Long matchSeq) throws ServiceException;

    void linkSummoner(LinkSummonerRequest request) throws DataNotExistException;

    void saveTierPoint(List<SaveTierPointRequest> request) throws AccessDeniedException;

    GroupSeasonEntity getGroupSeasonEntity(Long seasonSeq) throws DataNotExistException;

    List<GroupResponse> findMyGroups();

}