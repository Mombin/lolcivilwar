package kr.co.mcedu.riot.data.repository;

import kr.co.mcedu.riot.data.entity.SummonerSpellEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummonerSpellJpaRepository extends JpaRepository<SummonerSpellEntity,Long> {
}
