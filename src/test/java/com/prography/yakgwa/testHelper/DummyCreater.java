package com.prography.yakgwa.testHelper;

import com.prography.yakgwa.domain.meet.entity.Meet;
import com.prography.yakgwa.domain.meet.entity.MeetTheme;
import com.prography.yakgwa.domain.meet.entity.embed.VotePeriod;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.participant.entity.Participant;
import com.prography.yakgwa.domain.participant.entity.enumerate.MeetRole;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.place.entity.Place;
import com.prography.yakgwa.domain.place.entity.dto.PlaceInfoDto;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.entity.User;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.vote.entity.place.PlaceSlot;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeSlot;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeVoteJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.prography.yakgwa.domain.user.entity.AuthType.KAKAO;

@Component
public class DummyCreater {
    @Autowired
    MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    private MeetJpaRepository meetJpaRepository;
    @Autowired
    private PlaceJpaRepository placeJpaRepository;
    @Autowired
    private PlaceVoteJpaRepository placeVoteJpaRepository;
    @Autowired
    private PlaceSlotJpaRepository placeSlotJpaRepository;
    @Autowired
    private TimeVoteJpaRepository timeVoteJpaRepository;
    @Autowired
    private TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    private ParticipantJpaRepository participantJpaRepository;

    public MeetTheme createAndSaveMeetTheme(int id) {
        MeetTheme theme = MeetTheme.builder()
                .name("name" + id)
                .build();
        return meetThemeJpaRepository.save(theme);
    }

    public User createAndSaveUser(int id) {
        User user = User.builder()
                .name("user" + id).isNew(true).authId(""+id).authType(KAKAO)
                .build();
        return userJpaRepository.save(user);
    }

    public Participant createAndSaveParticipant(Meet saveMeet, User saveUser, MeetRole meetRole) {
        Participant participant = Participant.builder().meet(saveMeet).user(saveUser).meetRole(meetRole).build();
        return participantJpaRepository.save(participant);
    }

    public PlaceVote createAndSavePlaceVote(User saveUser, PlaceSlot andSavePlaceSlot) {
        PlaceVote placeVote = PlaceVote.builder().user(saveUser).placeSlot(andSavePlaceSlot).build();
        return placeVoteJpaRepository.save(placeVote);
    }

    public TimeVote createAndSaveTimeVote(TimeSlot saveTimeSlot, User saveUser) {
        TimeVote timeVote = TimeVote.builder().timeSlot(saveTimeSlot).user(saveUser).build();
        return timeVoteJpaRepository.save(timeVote);
    }

    public TimeSlot createAndSaveTimeSlot(Meet saveMeet, LocalDateTime time, boolean confirm) {
        return timeSlotJpaRepository.save(TimeSlot.builder().meet(saveMeet).time(time).confirm(confirm).build());
    }

    public Meet createAndSaveMeet(int id, MeetTheme saveMeetTheme, int validInviteHour) {
        Meet meet = Meet.builder()
                .title("title" + id).validInviteHour(validInviteHour).period(new VotePeriod(LocalDate.now(), LocalDate.now().plusDays(1L))).meetTheme(saveMeetTheme)
                .build();
        return meetJpaRepository.save(meet);
    }

    public Place createAndSavePlace(int id) {
        Place place = PlaceInfoDto.builder()
                .mapx("" + id).mapy("" + id).link("link" + id).address("address" + id).roadAddress("roadAddress" + id).category("category" + id).description("description" + id).title("title" + id).telephone("telephone" + id)
                .build().toEntity();
        return placeJpaRepository.save(place);
    }

    public PlaceSlot createAndSavePlaceSlot(Place savePlace, Meet saveMeet, boolean confirm) {
        return placeSlotJpaRepository.save(PlaceSlot.builder()
                .place(savePlace).meet(saveMeet).confirm(confirm)
                .build());
    }
}
