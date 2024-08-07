package com.prography.yakgwa.testHelper;

import com.prography.yakgwa.domain.common.alarm.repository.AlarmJpaRepository;
import com.prography.yakgwa.domain.magazine.repository.ImageJpaRepository;
import com.prography.yakgwa.domain.magazine.repository.MagazineJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.repository.SignoutUserJpaRepository;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
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
    AlarmJpaRepository alarmJpaRepository;
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
    @Autowired
    MagazineJpaRepository magazineJpaRepository;
    @Autowired
    ImageJpaRepository imageJpaRepository;
    @Autowired
    SignoutUserJpaRepository signoutUserJpaRepository;
    public void deleteAll() {
        alarmJpaRepository.deleteAll();
        signoutUserJpaRepository.deleteAll();
        imageJpaRepository.deleteAll();
        magazineJpaRepository.deleteAll();
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
