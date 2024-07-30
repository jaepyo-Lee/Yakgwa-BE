package com.prography.yakgwa.domain.common;

import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class RepositoryDeleter {
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    MeetJpaRepository meetJpaRepository;
    @Autowired
    MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    PlaceVoteJpaRepository placeVoteJpaRepository;
    @Autowired
    PlaceSlotJpaRepository placeSlotJpaRepository;
    @Autowired
    TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    TimeVoteJpaRepository timeVoteJpaRepository;
    @Autowired
    ParticipantJpaRepository participantJpaRepository;
    @Autowired
    PlaceJpaRepository placeJpaRepository;

    public void deleteAll() {
        placeVoteJpaRepository.deleteAll();
        placeSlotJpaRepository.deleteAll();
        placeJpaRepository.deleteAll();
        timeVoteJpaRepository.deleteAll();
        timeSlotJpaRepository.deleteAll();
        participantJpaRepository.deleteAll();
        userJpaRepository.deleteAll();
        meetJpaRepository.deleteAll();
        meetThemeJpaRepository.deleteAll();
    }
}
