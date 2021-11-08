package kr.co.mcedu.helper.service;

import kr.co.mcedu.config.exception.AlreadyDataExistException;
import kr.co.mcedu.helper.model.SuggestionRequest;

public interface HelperService {
    boolean saveSuggestion(SuggestionRequest suggestionRequest) throws AlreadyDataExistException;
}