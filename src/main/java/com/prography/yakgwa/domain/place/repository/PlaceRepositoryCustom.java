package com.prography.yakgwa.domain.place.repository;

import com.prography.yakgwa.domain.place.entity.Place;

import java.util.List;

public interface PlaceRepositoryCustom {
    List<Place> findByTitleAndMapxAndMapyIn(List<String> titles, List<String> mapxs, List<String> mapys);
}
