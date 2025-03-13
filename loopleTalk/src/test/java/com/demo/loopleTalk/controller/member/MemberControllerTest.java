package com.demo.loopleTalk.controller.member;

import com.demo.loopleTalk.domain.member.Member;
import com.demo.loopleTalk.dto.member.AddMemberRequest;
import com.demo.loopleTalk.dto.member.UpdateMemberRequest;
import com.demo.loopleTalk.repository.member.MemberRepository;
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
class MemberControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        memberRepository.deleteAll();
    }

    @DisplayName("addMember: 멤버 추가에 성공한다.")
    @Test
    public void 멤버_추가에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        final String url = "/member";
        final AddMemberRequest addMemberRequest = new AddMemberRequest(name, email, password, phone, birth);
        final String requestBody = objectMapper.writeValueAsString(addMemberRequest);

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        resultActions.andExpect(status().isCreated());

        List<Member> members = memberRepository.findAll();

        assertThat(members.size()).isEqualTo(1);
        assertThat(members.getFirst().getName()).isEqualTo(name);
        assertThat(members.getFirst().getEmail()).isEqualTo(email);
        assertThat(members.getFirst().getPassword()).isEqualTo(password);
        assertThat(members.getFirst().getPhone()).isEqualTo(phone);
        assertThat(members.getFirst().getBirth()).isEqualTo(birth);
    }

    @DisplayName("findAllMember: 전체 멤버 조회에 성공한다.")
    @Test
    public void 전체_멤버_조회에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        final String name2 = "test2";
        final String email2 = "test2@email.com";
        final String password2 = "test2";
        final String phone2 = "010-5678-1234";
        final LocalDate birth2 = LocalDate.parse("2000-02-02");

        memberRepository.save(
                Member.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phone(phone)
                        .birth(birth)
                        .build()
        );

        memberRepository.save(
                Member.builder()
                        .name(name2)
                        .email(email2)
                        .password(password2)
                        .phone(phone2)
                        .birth(birth2)
                        .build()
        );

        final String url = "/member";

        //when
        ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[1].name").value(name2))
                .andExpect(jsonPath("$[0].email").value(email))
                .andExpect(jsonPath("$[1].email").value(email2));
    }

    @DisplayName("findMember: 멤버 조회에 성공한다.")
    @Test
    public void 멤버_조회에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        memberRepository.save(
                Member.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phone(phone)
                        .birth(birth)
                        .build()
        );

        final String url = "/member/{email}";

        //when
        ResultActions resultActions = mockMvc.perform(get(url, email));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").value(password))
                .andExpect(jsonPath("$.phone").value(phone))
                .andExpect(jsonPath("$.birth").value(birth.toString()));
    }

    @DisplayName("updateMember: 멤버 수정에 성공한다.")
    public void 멤버_수정에_성공한다() throws Exception {

        //given
        final String name = "test";
        final String email = "test@email.com";
        final String password = "test";
        final String phone = "010-1234-5678";
        final LocalDate birth = LocalDate.parse("2000-01-01");

        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .phone(phone)
                .birth(birth)
                .build()
        );

        final String url = "/member/{email}";
        final String newName = "new name";
        final LocalDate newBirth = LocalDate.parse("2000-02-01");

        UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest(Member.builder()
                .name(newName)
                .password(password)
                .email(email)
                .birth(newBirth)
                .build()
        );

        //when
        final ResultActions resultActions = mockMvc.perform(put(url, email)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateMemberRequest)));

        //then
        resultActions.andExpect(status().isOk());

        Member updateMember = memberRepository.findByEmail(email);

        assertThat(updateMember.getName()).isEqualTo(newName);
        assertThat(updateMember.getBirth()).isEqualTo(newBirth);
    }
}