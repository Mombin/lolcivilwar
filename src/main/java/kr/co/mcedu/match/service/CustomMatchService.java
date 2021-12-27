package kr.co.mcedu.match.service;

import kr.co.mcedu.config.exception.ServiceException;
import kr.co.mcedu.match.model.request.CustomMatchSaveRequest;
import kr.co.mcedu.match.model.request.DiceRequest;
import kr.co.mcedu.match.model.response.DiceResponse;
import kr.co.mcedu.riot.engine.response.RiotApiResponse;

import java.util.List;
import java.util.Map;

public interface CustomMatchService {
    void saveCustomMatchResult(CustomMatchSaveRequest customMatchSaveRequest) throws ServiceException;
    Map<String, DiceResponse> randomDice(DiceRequest request) throws ServiceException;
    RiotApiResponse getGameInfo(List<String> encryptedSummonerIdList);
}