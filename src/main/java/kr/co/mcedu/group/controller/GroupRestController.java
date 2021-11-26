package kr.co.mcedu.group.controller;

import kr.co.mcedu.config.exception.AccessDeniedException;
import kr.co.mcedu.config.exception.AlreadyDataExistException;
import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.model.response.CustomUserResponse;
import kr.co.mcedu.group.entity.GroupAuthEnum;
import kr.co.mcedu.group.model.GroupResponse;
import kr.co.mcedu.group.model.GroupSaveRequest;
import kr.co.mcedu.group.model.request.*;
import kr.co.mcedu.group.model.response.CustomUserSynergyResponse;
import kr.co.mcedu.group.service.GroupService;
import kr.co.mcedu.utils.ResponseWrapper;
import kr.co.mcedu.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group")
class GroupRestController {
    private final GroupService groupService;


    @PostMapping("")
    public Object saveGroup(@RequestBody GroupSaveRequest groupSaveRequest) throws ServiceException {
        ResponseWrapper result;
        try {
            Long groupSeq = groupService.makeGroup(groupSaveRequest);
            if (groupSeq == 0L) {
                result = ResponseWrapper.fail();
            } else {
                result = new ResponseWrapper().setData(groupSeq);
                SessionUtils.refreshAccessToken();
            }
        } catch (AlreadyDataExistException e) {
            result = ResponseWrapper.fail("이미 해당 아이디로 생성된 그룹이 있습니다.");
        }
        return result.build();
    }

    @PostMapping("/v1/my")
    public Object getMyGroupV1() {
        return new ResponseWrapper().setData(groupService.findMyGroups()).build();
    }

    @PostMapping("/my")
    public Object getMyGroupV0() throws AccessDeniedException {
        List<GroupResponse> list = groupService.findMyGroupsWithDefaultSeason();
        list.forEach(it -> it.getCustomUser().sort(Comparator.comparing(CustomUserResponse::getTierPoint).reversed()));
        return new ResponseWrapper().setData(list).build();
    }

    @PostMapping("/my-match")
    public Object getMyMatchGroup() throws AccessDeniedException {
        List<GroupResponse> list = groupService.findMyGroupsWithDefaultSeason().stream().filter(it -> GroupAuthEnum.isMatchableAuth(
                Optional.ofNullable(it.getAuth()).orElse(GroupAuthEnum.NONE))).collect(Collectors.toList());
        list.forEach(it -> it.getCustomUser().sort(Comparator.comparing(CustomUserResponse::getLastDate,
                Comparator.nullsLast(Comparator.reverseOrder()))));
        return new ResponseWrapper().setData(list).build();
    }

    @PostMapping("/my-manage")
    public Object getMyManagerGroup() throws AccessDeniedException {
        List<GroupResponse> list = groupService.findMyGroupsWithDefaultSeason().stream()
                                               .filter(it -> GroupAuthEnum.isManageableAuth(it.getAuth()))
                                               .collect(Collectors.toList());
        list.forEach(it -> it.getCustomUser().clear());
        return new ResponseWrapper().setData(list).build();
    }

    @GetMapping("/auth/{groupSeq}")
    public Object getAuthUserList(@PathVariable Long groupSeq) throws ServiceException {
        return new ResponseWrapper().setData(groupService.getAuthUserList(groupSeq)).build();
    }

    /**
     * 새로운 유저를 그룹에 등록함
     *
     * @param customUserSaveRequest 등록 폼
     * @return 결과
     */
    @PostMapping("/user")
    public Object saveCustomUser(@RequestBody CustomUserSaveRequest customUserSaveRequest) {
        try {
            groupService.addSummonerInGroup(customUserSaveRequest);
        } catch (ServiceException e) {
            return ResponseWrapper.fail(e.getViewMessage()).build();
        }

        return new ResponseWrapper().build();
    }

    @DeleteMapping("/user")
    public Object deleteCustomUser(@RequestBody CustomUserDeleteRequest customUserDeleteRequest) {
        try {
            groupService.deleteSummonerInGroup(customUserDeleteRequest);
        } catch (ServiceException e) {
            return ResponseWrapper.fail(e.getViewMessage()).build();
        }
        return new ResponseWrapper().build();
    }

    /**
     * 닉네임, 아이디 수정
     *
     * @param customUserModifyRequest 수정 데이터
     * @return 결과
     */
    @PutMapping("/user")
    public Object modifyCustomUser(@RequestBody CustomUserModifyRequest customUserModifyRequest) {
        try {
            groupService.modifySummonerInGroup(customUserModifyRequest);
        } catch (ServiceException e) {
            return ResponseWrapper.fail(e.getViewMessage()).build();
        }
        return new ResponseWrapper().build();
    }

    @GetMapping("/synergy")
    public Object getSynergy(@ModelAttribute CustomUserSynergyRequest customUserSynergyRequest)
            throws ServiceException {
        log.info("GroupRestController > getSynergy : {}", customUserSynergyRequest.toString());
        CustomUserSynergyResponse data = groupService.calculateSynergy(customUserSynergyRequest);
        return new ResponseWrapper().setData(data).build();
    }

    @PostMapping("/match/{groupSeq}/{page}")
    public Object getMatch(@PathVariable Long groupSeq, @PathVariable Integer page) throws Exception {
        return new ResponseWrapper().setData(groupService.getMatches(groupSeq, page)).build();
    }

    @DeleteMapping("/match/{matchSeq}")
    public Object deleteMatch(@PathVariable Long matchSeq) throws ServiceException {
        return new ResponseWrapper().setData(groupService.deleteMatch(matchSeq)).build();
    }

    @PostMapping("/v1/link/summoner")
    public Object linkSummoner(@RequestBody LinkSummonerRequest request) throws DataNotExistException {
        log.info("GroupRestController > linkSummoner: {}", request.toString());
        groupService.linkSummoner(request);
        return new ResponseWrapper().build();
    }

    @GetMapping("/v1/personal/result")
    public Object personalResult(@ModelAttribute PersonalResultRequest request) throws Exception {
        log.info("GroupRestController  > personalResult: {}", request.toString());
        return new ResponseWrapper().setData(groupService.getPersonalResult(request)).build();
    }

    @PostMapping("/tier-point")
    public Object saveTierPoint(@RequestBody List<SaveTierPointRequest> request) throws AccessDeniedException {
        log.info("GroupRestController > saveTierPoint: {}", request.toString());
        groupService.saveTierPoint(request);
        return new ResponseWrapper().build();
    }
}