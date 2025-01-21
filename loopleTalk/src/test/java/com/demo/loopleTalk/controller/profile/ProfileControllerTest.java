package com.demo.loopleTalk.controller.profile;

import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.domain.profile.Profile;
import com.demo.loopleTalk.dto.profile.AddProfileRequest;
import com.demo.loopleTalk.dto.profile.UpdateProfileRequest;
import com.demo.loopleTalk.repository.member.MemberRepository;
import com.demo.loopleTalk.repository.profile.ProfileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        profileRepository.deleteAll();
    }

    @DisplayName("addProfile: 프로필 추가에 성공한다.")
    @Test
    public void 프로필_추가에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        Member savedMember = memberRepository.save(
                Member.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phone(phone)
                        .birth(birth)
                        .build()
        );

        final String nickname = "test";
        final String mbti = "ESFP";
        final String job = "백숙";
        final boolean gender = true;
        final String location = "서울";
        final String intro = "안녕하세요. 테스트입니다.";
        final double positionX = 12.12;
        final double positionY = 12.12;
        final Long memberId = savedMember.getMemberId();

        final String url = "/profile";
        final AddProfileRequest addProfileRequest = new AddProfileRequest(
                nickname, mbti, job, gender, location, intro, positionX, positionY, memberId);
        final String requestBody = objectMapper.writeValueAsString(addProfileRequest);

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        resultActions.andExpect(status().isCreated());

        List<Profile> profiles = profileRepository.findAll();

        assertThat(profiles.size()).isEqualTo(1);
        assertThat(profiles.getFirst().getNickname()).isEqualTo(nickname);
        assertThat(profiles.getFirst().getMbti()).isEqualTo(mbti);
        assertThat(profiles.getFirst().getJob()).isEqualTo(job);
        assertThat(profiles.getFirst().isGender()).isEqualTo(gender);
        assertThat(profiles.getFirst().getLocation()).isEqualTo(location);
        assertThat(profiles.getFirst().getIntro()).isEqualTo(intro);
        assertThat(profiles.getFirst().getPositionX()).isEqualTo(positionX);
        assertThat(profiles.getFirst().getPositionY()).isEqualTo(positionY);
        assertThat(profiles.getFirst().getProfileId()).isEqualTo(savedMember.getMemberId());
    }

    @DisplayName("findProfile: 프로필 조회에 성공한다.")
    @Test
    public void 프로필_조회에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        Member savedMember = memberRepository.save(
                Member.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phone(phone)
                        .birth(birth)
                        .build()
        );

        final String nickname = "test";
        final String mbti = "ESFP";
        final String job = "백숙";
        final boolean gender = true;
        final String location = "서울";
        final String intro = "안녕하세요. 테스트입니다.";
        final double positionX = 12.12;
        final double positionY = 12.12;

        Profile savedProfile = profileRepository.save(
                Profile.builder()
                        .nickname(nickname)
                        .mbti(mbti)
                        .job(job)
                        .gender(gender)
                        .location(location)
                        .intro(intro)
                        .positionX(positionX)
                        .positionY(positionY)
                        .member(savedMember)
                        .build()
        );

        final String url = "/profile/{id}";

        //when
        ResultActions resultActions = mockMvc.perform(get(url, savedProfile.getProfileId()));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.mbti").value(mbti))
                .andExpect(jsonPath("$.job").value(job))
                .andExpect(jsonPath("$.gender").value(gender))
                .andExpect(jsonPath("$.location").value(location))
                .andExpect(jsonPath("$.intro").value(intro))
                .andExpect(jsonPath("$.positionX").value(positionX))
                .andExpect(jsonPath("$.positionY").value(positionY));
    }

    @DisplayName("findMemberByProfile: 프로필로 멤버 조회에 성공한다.")
    @Test
    public void 프로필로_멤버_조회에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        Member savedMember = memberRepository.save(
                Member.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phone(phone)
                        .birth(birth)
                        .build()
        );

        final String nickname = "test";
        final String mbti = "ESFP";
        final String job = "백숙";
        final boolean gender = true;
        final String location = "서울";
        final String intro = "안녕하세요. 테스트입니다.";
        final double positionX = 12.12;
        final double positionY = 12.12;

        Profile savedProfile = profileRepository.save(
                Profile.builder()
                        .nickname(nickname)
                        .mbti(mbti)
                        .job(job)
                        .gender(gender)
                        .location(location)
                        .intro(intro)
                        .positionX(positionX)
                        .positionY(positionY)
                        .member(savedMember)
                        .build()
        );

        final String url = "/profile/info/{id}";

        //when
        ResultActions resultActions = mockMvc.perform(get(url, savedProfile.getProfileId()))
                .andExpect(status().isOk());

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").value(password))
                .andExpect(jsonPath("$.phone").value(phone))
                .andExpect(jsonPath("$.birth").value(birth.toString()));
    }

    @DisplayName("findAllProfile: 전체 프로필 조회에 성공한다.")
    @Test
    public void 전체_프로필_조회에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        Member savedMember = memberRepository.save(
                Member.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phone(phone)
                        .birth(birth)
                        .build()
        );

        final String nickname = "test";
        final String mbti = "ESFP";
        final String job = "백숙";
        final boolean gender = true;
        final String location = "서울";
        final String intro = "안녕하세요. 테스트입니다.";
        final double positionX = 12.12;
        final double positionY = 12.12;

        profileRepository.save(
                Profile.builder()
                        .nickname(nickname)
                        .mbti(mbti)
                        .job(job)
                        .gender(gender)
                        .location(location)
                        .intro(intro)
                        .positionX(positionX)
                        .positionY(positionY)
                        .member(savedMember)
                        .build()
        );

        final String name2 = "test2";
        final String email2 = "test2@email.com";
        final String password2 = "test2";
        final String phone2 = "010-1234-5679";
        final LocalDate birth2 = LocalDate.parse("2000-01-02");

        Member savedMember2 = memberRepository.save(
                Member.builder()
                        .name(name2)
                        .email(email2)
                        .password(password2)
                        .phone(phone2)
                        .birth(birth2)
                        .build()
        );

        final String nickname2 = "test2";
        final String mbti2 = "INTJ";
        final String job2 = "학생";
        final boolean gender2 = false;
        final String location2 = "강원도";
        final String intro2 = "안녕하세요. 테스트2입니다.";
        final double positionX2 = 132.12;
        final double positionY2 = 132.12;

        profileRepository.save(
                Profile.builder()
                        .nickname(nickname2)
                        .mbti(mbti2)
                        .job(job2)
                        .gender(gender2)
                        .location(location2)
                        .intro(intro2)
                        .positionX(positionX2)
                        .positionY(positionY2)
                        .member(savedMember2)
                        .build()
        );

        final String url = "/profile";

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nickname").value(nickname))
                .andExpect(jsonPath("$[1].nickname").value(nickname2))
                .andExpect(jsonPath("$[0].gender").value(gender))
                .andExpect(jsonPath("$[1].gender").value(gender2))
                .andExpect(jsonPath("$[0].location").value(location))
                .andExpect(jsonPath("$[1].location").value(location2));
    }

    @DisplayName("deleteProfile: 프로필 삭제에 성공한다.")
    @Test
    public void 프로필_삭제에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        Member savedMember = memberRepository.save(
                Member.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phone(phone)
                        .birth(birth)
                        .build()
        );

        final String nickname = "test";
        final String mbti = "ESFP";
        final String job = "백숙";
        final boolean gender = true;
        final String location = "서울";
        final String intro = "안녕하세요. 테스트입니다.";
        final double positionX = 12.12;
        final double positionY = 12.12;

        Profile savedProfile = profileRepository.save(
                Profile.builder()
                        .nickname(nickname)
                        .mbti(mbti)
                        .job(job)
                        .gender(gender)
                        .location(location)
                        .intro(intro)
                        .positionX(positionX)
                        .positionY(positionY)
                        .member(savedMember)
                        .build()
        );

        final String url = "/profile/{id}";

        //when
        mockMvc.perform(delete(url, savedProfile.getProfileId()))
                .andExpect(status().isOk());

        //then
        List<Profile> profiles = profileRepository.findAll();
        List<Member> members = memberRepository.findAll();

        assertThat(profiles).isEmpty();
        assertThat(members).isEmpty();
    }

    @DisplayName("updateProfile: 프로필 수정에 성공한다.")
    @Test
    public void 프로필_수정에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        Member savedMember = memberRepository.save(
                Member.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phone(phone)
                        .birth(birth)
                        .build()
        );

        final String nickname = "test";
        final String mbti = "ESFP";
        final String job = "백숙";
        final boolean gender = true;
        final String location = "서울";
        final String intro = "안녕하세요. 테스트입니다.";
        final double positionX = 12.12;
        final double positionY = 12.12;

        Profile savedProfile = profileRepository.save(
                Profile.builder()
                        .nickname(nickname)
                        .mbti(mbti)
                        .job(job)
                        .gender(gender)
                        .location(location)
                        .intro(intro)
                        .positionX(positionX)
                        .positionY(positionY)
                        .member(savedMember)
                        .build()
        );

        final String url = "/profile/{id}";

        final String newNickname = "new nickname";
        final String newIntro = "안녕하세요. 새로운 테스트입니다.";
        final UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(
                newNickname, mbti, job, location, newIntro, positionX, positionY
        );

        //when
        ResultActions resultActions = mockMvc.perform(put(url, savedProfile.getProfileId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateProfileRequest)));

        //then
        resultActions.andExpect(status().isOk());

        Profile updateProfile = profileRepository.findById(savedProfile.getProfileId())
                .orElseThrow(() -> new IllegalArgumentException("NOT FOUND"));

        assertThat(updateProfile.getNickname()).isEqualTo(newNickname);
        assertThat(updateProfile.getIntro()).isEqualTo(newIntro);
    }
}