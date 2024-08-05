package com.prography.yakgwa.domain.magazine.repository;

import com.prography.yakgwa.domain.magazine.entity.Magazine;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.prography.yakgwa.domain.magazine.entity.QMagazine.magazine;

@Repository
@RequiredArgsConstructor
public class MagazineRepositoryImpl {
    private final JPAQueryFactory queryFactory;

    /**
     * Todo
     * Work) Test Code
     * Write-Date) 2024-07-28, 일, 1:17
     * Finish-Date)
     */
    public Page<Magazine> searchPage(Pageable pageable) {
        List<Magazine> content = queryFactory
                .selectFrom(magazine)
                .where(magazine.open)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int count = queryFactory
                .select(magazine.count())
                .from(magazine)
                .where(magazine.open)
                .fetch().size();

        return new PageImpl<>(content, pageable, count);
    }

}
