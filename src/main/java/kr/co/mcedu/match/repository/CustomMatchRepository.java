package kr.co.mcedu.match.repository;

import kr.co.mcedu.group.entity.GroupEntity;
import kr.co.mcedu.match.entity.CustomMatchEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomMatchRepository extends PagingAndSortingRepository<CustomMatchEntity, Long> {
    Page<CustomMatchEntity> findByGroupOrderByMatchSeqDesc(GroupEntity group, Pageable pageable);
}