package kr.co.mcedu.test.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mcedu.test.entity.TestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import static kr.co.mcedu.test.entity.QTestEntity.testEntity;

@Repository
@RequiredArgsConstructor
public class TestRepository {
    private final EntityManager entityManager;
    private JPAQueryFactory jpaQueryFactory;
    @PostConstruct
    public void init() {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    public TestEntity getTestEntity() {
        return jpaQueryFactory.selectFrom(testEntity)
                              .where(testEntity.propertyName.eq("RIOT_PROPERTY"))
                              .fetchOne();
    }
}
