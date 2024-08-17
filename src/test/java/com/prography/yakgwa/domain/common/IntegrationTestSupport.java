package com.prography.yakgwa.domain.common;

import com.prography.yakgwa.domain.auth.service.AuthService;
import com.prography.yakgwa.domain.common.alarm.repository.AlarmJpaRepository;
import com.prography.yakgwa.domain.common.impl.AwsS3Util;
import com.prography.yakgwa.domain.common.schedule.TaskScheduleManager;
import com.prography.yakgwa.domain.magazine.repository.ImageJpaRepository;
import com.prography.yakgwa.domain.magazine.repository.MagazineJpaRepository;
import com.prography.yakgwa.domain.magazine.service.MagazineService;
import com.prography.yakgwa.domain.meet.impl.MeetStatusJudger;
import com.prography.yakgwa.domain.meet.impl.MeetWriter;
import com.prography.yakgwa.domain.meet.repository.MeetJpaRepository;
import com.prography.yakgwa.domain.meet.repository.MeetThemeJpaRepository;
import com.prography.yakgwa.domain.meet.service.MeetCreateService;
import com.prography.yakgwa.domain.meet.service.MeetService;
import com.prography.yakgwa.domain.meet.service.MeetThemeService;
import com.prography.yakgwa.domain.participant.impl.ParticipantWriter;
import com.prography.yakgwa.domain.participant.repository.ParticipantJpaRepository;
import com.prography.yakgwa.domain.participant.service.ParticipantService;
import com.prography.yakgwa.domain.place.impl.PlaceWriter;
import com.prography.yakgwa.domain.place.repository.PlaceJpaRepository;
import com.prography.yakgwa.domain.user.repository.UserJpaRepository;
import com.prography.yakgwa.domain.user.service.UserService;
import com.prography.yakgwa.domain.vote.entity.place.PlaceVote;
import com.prography.yakgwa.domain.vote.entity.time.TimeVote;
import com.prography.yakgwa.domain.vote.repository.PlaceSlotJpaRepository;
import com.prography.yakgwa.domain.vote.repository.PlaceVoteJpaRepository;
import com.prography.yakgwa.domain.vote.repository.TimeSlotJpaRepository;
import com.prography.yakgwa.domain.vote.service.PlaceConfirm;
import com.prography.yakgwa.domain.vote.service.TimeConfirm;
import com.prography.yakgwa.domain.vote.service.VoteExecuter;
import com.prography.yakgwa.domain.vote.service.VoteFinder;
import com.prography.yakgwa.domain.vote.service.impl.VoteCounter;
import com.prography.yakgwa.domain.vote.service.place.PlaceSlotService;
import com.prography.yakgwa.domain.vote.service.place.PlaceVoteExecuteService;
import com.prography.yakgwa.domain.vote.service.place.res.PlaceInfosByMeetStatus;
import com.prography.yakgwa.domain.vote.service.time.req.EnableTimeRequestDto;
import com.prography.yakgwa.domain.vote.service.time.res.TimeInfosByMeetStatus;
import com.prography.yakgwa.global.repository.RedisRepository;
import com.prography.yakgwa.global.util.jwt.TokenProvider;
import com.prography.yakgwa.testHelper.DummyCreater;
import com.prography.yakgwa.testHelper.RepositoryDeleter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class IntegrationTestSupport {
    @Autowired
    protected MeetThemeJpaRepository meetThemeJpaRepository;
    @Autowired
    protected MeetStatusJudger meetStatusJudger;
    @Autowired
    protected MeetThemeService meetThemeService;
    @Autowired
    protected RepositoryDeleter deleter;
    @Autowired
    protected DummyCreater dummyCreater;
    @Autowired
    protected UserJpaRepository userJpaRepository;
    @Autowired
    protected PlaceJpaRepository placeJpaRepository;
    @Autowired
    protected MagazineService magazineService;
    @Autowired
    protected MagazineJpaRepository magazineJpaRepository;
    @Autowired
    protected ImageJpaRepository imageJpaRepository;
    @Autowired
    @Qualifier("timeVoteExecuteService")
    protected VoteExecuter<TimeVote, EnableTimeRequestDto> timeVoteExecuter;
    @Autowired
    protected TimeSlotJpaRepository timeSlotJpaRepository;
    @Autowired
    protected MeetJpaRepository meetJpaRepository;
    @Autowired
    protected MeetWriter meetWriter;
    @Autowired
    protected MeetCreateService meetCreateService;
    @Autowired
    protected PlaceSlotJpaRepository placeSlotJpaRepository;
    @Autowired
    protected ParticipantJpaRepository participantJpaRepository;
    @Autowired
    protected MeetService meetService;

    @Autowired
    protected ParticipantWriter participantWriter;

    @Autowired
    protected ParticipantService participantService;

    @Autowired
    @Qualifier("placeVoteFindService")
    protected VoteFinder<PlaceInfosByMeetStatus> placeVoteFinder;

    @Autowired
    protected UserService userService;

    @Autowired
    @Qualifier("timeVoteFindService")
    protected VoteFinder<TimeInfosByMeetStatus> timeVoteFinder;

    @Autowired
    @Qualifier("placeVoteExecuteService")
    protected VoteExecuter<PlaceVote, Set<Long>> placeVoteExecuter;

    @Autowired
    protected PlaceSlotService placeSlotService;
    @Autowired
    protected VoteCounter voteCounter;

    @Autowired
    protected PlaceVoteJpaRepository placeVoteJpaRepository;

    @MockBean
    protected TaskScheduleManager scheduler;
    @MockBean
    protected AwsS3Util awsS3Util;

    @Autowired
    protected AuthService authService;

    @MockBean
    protected TokenProvider tokenProvider;

    @MockBean
    protected RedisRepository redisRepository;
    @Autowired
    protected AlarmJpaRepository alarmJpaRepository;
    @Autowired
    protected PlaceVoteExecuteService placeVoteExecuteService;
    @Autowired
    protected PlaceWriter placeWriter;
    @Autowired
    protected PlaceConfirm placeConfirm;
    @Autowired
    protected TimeConfirm timeConfirm;
}
