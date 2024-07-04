package com.prography.yakgwa.domain.vote.controller.req;

import lombok.Getter;

import java.util.List;

@Getter
public class VotePlaceRequest {
    private List<Long> currentVotePlaceSlotIds;
}
