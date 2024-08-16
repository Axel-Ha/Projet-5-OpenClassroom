package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class SessionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    public void setupAuthAndToken(){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("yoga@studio.com","test!1234")
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    void findSessionByIdTest_ExistSession() throws Exception {
        mockMvc.perform(get("/api/session/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Yoga session 1"));
    }

    @Test
    public void findSessionByIdTest_SessionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/session/999")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findAllSessionTest() throws Exception {
        mockMvc.perform(get("/api/session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[1].id").value("2"));
    }

    @Test
    void createSessionTest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga Session Create");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Yoga Session Description");

        String response = mockMvc.perform(post("/api/session")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Session Create"))
                .andReturn().getResponse().getContentAsString();

        SessionDto sessionFromResponse = objectMapper.readValue(response,sessionDto.getClass());

        Session session = sessionRepository.findById(sessionFromResponse.getId()).orElseThrow(null);
        sessionService.delete(session.getId());
    }

    @Test
    void updateSessionTest_Valid() throws Exception {
        Session session = Session.builder()
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .build();
        sessionService.create(session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga session updated");
        sessionDto.setDescription("Description updated");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDate(new Date());

        mockMvc.perform(put("/api/session/"+ session.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga session updated"))
                .andExpect(jsonPath("$.description").value("Description updated"));
        sessionService.delete(session.getId());
    }

    @Test
    void updateSessionTest_SessionDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/session/999")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSessionTest_Valid() throws Exception {
        Session session = Session.builder()
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .build();
        sessionService.create(session);

        mockMvc.perform(delete("/api/session/" + session.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSessionTest_SessionDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/session/999")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void participateSessionTest_Valid() throws Exception {
        Session session = Session.builder()
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(new ArrayList<>())
                .build();
        sessionService.create(session);

        mockMvc.perform(post("/api/session/"+session.getId()+"/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
        sessionService.delete(session.getId());
    }

    @Test
    void participateSessionTest_SessionDoesNotExist() throws Exception {
        mockMvc.perform(post("/api/session/999/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }


    @Test
    void participateSessionTest_UserDoesNotExist() throws Exception {
        Session session = Session.builder()
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(new ArrayList<>())
                .build();
        sessionService.create(session);

        mockMvc.perform(post("/api/session/"+session.getId()+"/participate/25")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
        sessionService.delete(session.getId());
    }

    @Test
    void participateSessionTest_UserAlreadyParticipate() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .lastName("TANNER")
                .admin(true)
                .firstName("Jean")
                .password("1234")
                .build();
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        Session session = Session.builder()
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(userList)
                .build();
        sessionService.create(session);

        mockMvc.perform(post("/api/session/"+session.getId()+"/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());

        sessionService.delete(session.getId());
    }

    @Test
    void noLongerParticipateSessionTest_Valid() throws Exception {
        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .lastName("TANNER")
                .admin(true)
                .firstName("Jean")
                .password("1234")
                .build();
        List<User> userList = new ArrayList<User>();
        userList.add(user);
        Session session = Session.builder()
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(userList)
                .build();
        sessionService.create(session);

        mockMvc.perform(delete("/api/session/"+session.getId()+"/participate/"+user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        sessionService.delete(session.getId());
    }

    @Test
    void noLongerParticipateSessionTest_UserDoesNotParticipate() throws Exception {
        Session session = Session.builder()
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(new ArrayList<>())
                .build();
        sessionService.create(session);

        mockMvc.perform(delete("/api/session/"+session.getId()+"/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());

        sessionService.delete(session.getId());
    }
    @Test
    void noLongerParticipateSessionTest_SessionDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest());
    }
}
