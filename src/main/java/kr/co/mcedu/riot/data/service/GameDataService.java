package kr.co.mcedu.riot.data.service;

import kr.co.mcedu.config.exception.AccessDeniedException;

public interface GameDataService {
    void insertChampionData() throws AccessDeniedException;

    void insertSpellData() throws AccessDeniedException;
}
