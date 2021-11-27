package kr.co.mcedu.group.service;

import kr.co.mcedu.config.exception.DataNotExistException;
import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.group.model.request.GroupResultRequest;
import kr.co.mcedu.group.model.response.CustomUserResponse;
import kr.co.mcedu.group.model.response.CustomUserSynergyResponse;

import java.util.List;

public interface GroupResultService {
    List<CustomUserResponse> getRankData(GroupResultRequest request) throws ServiceException;

    List<CustomUserResponse> getCustomUserBySeason(GroupResultRequest request) throws DataNotExistException;

    List<CustomUserResponse> getMatchAttendees(GroupResultRequest request) throws ServiceException;

    CustomUserSynergyResponse calculateSynergy(GroupResultRequest groupResultRequest) throws ServiceException;
}
