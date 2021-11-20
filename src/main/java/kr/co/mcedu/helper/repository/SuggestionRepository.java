package kr.co.mcedu.helper.repository;

import kr.co.mcedu.helper.entity.SuggestionEntity;
import kr.co.mcedu.helper.entity.SuggestionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuggestionRepository extends JpaRepository<SuggestionEntity, SuggestionId> {
}