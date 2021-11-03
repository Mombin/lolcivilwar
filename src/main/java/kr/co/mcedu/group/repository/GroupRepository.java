package kr.co.mcedu.group.repository;

import kr.co.mcedu.group.entity.GroupEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {
    List<GroupEntity> findAllByOwner(String owner);

    @EntityGraph(attributePaths = {"customUser", "customUser.summonerEntity"})
    @Query("select distinct g from group g where g.groupSeq = :groupSeq")
    Optional<GroupEntity> findByIdFetch(Long groupSeq);
}