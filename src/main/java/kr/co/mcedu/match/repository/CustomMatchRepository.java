package kr.co.mcedu.match.repository;

import kr.co.mcedu.match.entity.CustomMatchEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomMatchRepository extends PagingAndSortingRepository<CustomMatchEntity, Long> {
}