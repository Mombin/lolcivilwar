package kr.co.mcedu.common.repository;

import kr.co.mcedu.common.entity.SystemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemRepository extends JpaRepository<SystemEntity, String> {
}
