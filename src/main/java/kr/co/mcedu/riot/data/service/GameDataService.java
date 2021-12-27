package kr.co.mcedu.riot.data.service;

import kr.co.mcedu.config.exception.AccessDeniedException;

public interface GameDataService {
    void insertChampionAndSpellData() throws AccessDeniedException;
}
