package kr.co.mcedu.match.service;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.match.model.request.CustomMatchSaveRequest;
import kr.co.mcedu.match.model.request.DiceRequest;
import kr.co.mcedu.match.model.response.DiceResponse;

import java.util.Map;

public interface CustomMatchService {
    void saveCustomMatchResult(CustomMatchSaveRequest customMatchSaveRequest) throws ServiceException;
    Map<String, DiceResponse> randomDice(DiceRequest request) throws ServiceException;
}