package kr.co.mcedu.group.service;

import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.group.model.GroupResponse;
import kr.co.mcedu.group.model.GroupSaveRequest;
import kr.co.mcedu.group.model.request.*;
import kr.co.mcedu.group.model.response.CustomUserSynergyResponse;
import kr.co.mcedu.group.model.response.GroupAuthResponse;
import kr.co.mcedu.group.model.response.PersonalResultResponse;
import kr.co.mcedu.match.model.response.MatchHistoryResponse;

import java.util.List;

public interface GroupService {
    GroupEntity getGroup(Long groupSeq);

    Long makeGroup(GroupSaveRequest groupSaveRequest);

    List<GroupResponse> findMyGroups();

    void addSummonerInGroup(CustomUserSaveRequest customUserSaveRequest);

    void deleteSummonerInGroup(CustomUserDeleteRequest customUserDeleteRequest);

    void modifySummonerInGroup(CustomUserModifyRequest customUserModifyRequest);

    CustomUserSynergyResponse calculateSynergy(CustomUserSynergyRequest customUserSynergyRequest);

    MatchHistoryResponse getMatches(Long groupSeq, Integer pageNum);

    Object deleteMatch(Long matchSeq);

    List<GroupAuthResponse> getAuthUserList(Long groupSeq);

    void linkSummoner(LinkSummonerRequest request);

    PersonalResultResponse getPersonalResult(PersonalResultRequest request);
}