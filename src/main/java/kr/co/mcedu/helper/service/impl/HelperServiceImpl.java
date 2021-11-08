package kr.co.mcedu.helper.service.impl;

import kr.co.mcedu.config.exception.AlreadyDataExistException;
import kr.co.mcedu.helper.entity.SuggestionEntity;
import kr.co.mcedu.helper.entity.SuggestionId;
import kr.co.mcedu.helper.model.SuggestionRequest;
import kr.co.mcedu.helper.repository.SuggestionRepository;
import kr.co.mcedu.helper.service.HelperService;
import kr.co.mcedu.utils.SessionUtils;
import kr.co.mcedu.utils.StringUtils;
import kr.co.mcedu.utils.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HelperServiceImpl implements HelperService {
    private final SuggestionRepository suggestionRepository;

    @Override
    public boolean saveSuggestion(SuggestionRequest suggestionRequest) throws AlreadyDataExistException {
        String userIp = SessionUtils.getIp();
        String context = suggestionRequest.getContext();
        if (StringUtils.isEmpty(userIp) || StringUtils.isEmpty(context)) {
            return false;
        }

        SuggestionId suggestionId = new SuggestionId(userIp, TimeUtils.timeDetail(TimeUtils.DAY_PATTERN));
        Optional<SuggestionEntity> suggestionOption = suggestionRepository.findById(suggestionId);
        if (suggestionOption.isPresent()) {
            throw new AlreadyDataExistException("이미 데이터가 존재합니다.");
        }

        SuggestionEntity suggestionEntity = new SuggestionEntity(suggestionId, context);
        suggestionRepository.save(suggestionEntity);
        return true;
    }
}