package kr.co.mcedu.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.mcedu.group.model.GroupAuthDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static kr.co.mcedu.group.entity.QGroupAuthEntity.groupAuthEntity;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @PostConstruct
    public void init() {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    public List<GroupAuthDto> getGroupAuthList(Long webUserSeq) {
        if (Objects.isNull(webUserSeq)) {
            return Collections.emptyList();
        }

        return queryFactory.select(Projections.bean(GroupAuthDto.class,
                                   groupAuthEntity.groupAuth,
                                   groupAuthEntity.group.groupSeq))
                           .from(groupAuthEntity)
                           .where(groupAuthEntity.webUser.userSeq.eq(webUserSeq))
                           .fetch();
    }
}
