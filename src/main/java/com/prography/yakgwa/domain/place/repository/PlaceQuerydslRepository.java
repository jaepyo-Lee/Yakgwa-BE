package com.prography.yakgwa.domain.place.repository;

import com.prography.yakgwa.domain.place.entity.Place;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.prography.yakgwa.domain.place.entity.QPlace.place;

@RequiredArgsConstructor
@Repository
public class PlaceQuerydslRepository implements PlaceRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Place> findByTitleAndMapxAndMapyIn(List<String> titles, List<String> mapxs, List<String> mapys) {
        BooleanBuilder builder = new BooleanBuilder();

        for (int i = 0; i < titles.size(); i++) {
            builder.or(place.title.eq(titles.get(i))
                    .and(place.mapx.eq(mapxs.get(i)))
                    .and(place.mapy.eq(mapys.get(i))));
        }

        return queryFactory
                .selectFrom(place)
                .where(builder)
                .fetch();
    }
}
