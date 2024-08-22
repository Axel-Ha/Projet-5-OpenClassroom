package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.openclassrooms.starterjwt.MapperConfig;
import com.openclassrooms.starterjwt.AuthConfig;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@Import(AuthConfig.class)
class SessionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private SessionMapper sessionMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;



    @WithMockUser(username="yoga@studio.com")
    @Test
    void findSessionByIdTest_ExistSession() throws Exception {
        Session session = new Session();
        session.setId(1L);
        session.setName("Yoga session 1");

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga session 1");

        given(sessionService.getById(1L)).willReturn(session);
        given(sessionMapper.toDto(session)).willReturn(sessionDto);
        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yoga session 1"));
    }

    @Test
    @WithMockUser(username="yoga@studio.com")
    public void findSessionByIdTest_SessionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/session/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="yoga@studio.com")
    @Test
    public void findAllSessionTest() throws Exception {
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        ArrayList<Session> sessionArrayList = new ArrayList<Session>();
        sessionArrayList.add(session);

        ArrayList<SessionDto> sessionDtoArrayList = new ArrayList<SessionDto>();
        sessionDtoArrayList.add(sessionDto);

        given(sessionService.findAll()).willReturn(sessionArrayList);
        given(sessionMapper.toDto(sessionArrayList)).willReturn(sessionDtoArrayList);

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @WithMockUser(username="yoga@studio.com")
    @Test
    void createSessionTest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);
        sessionDto.setName("Yoga Session Create");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Yoga Session Description");
        sessionDto.setDate(new Date());

        Session session = new Session();
        session.setId(1L);
        session.setName("Yoga Session Create");

        // Assume that sessionService.create will return the session object.
        given(sessionMapper.toEntity(sessionDto)).willReturn(session);
        given(sessionService.create(session)).willReturn(session);
        given(sessionMapper.toDto(session)).willReturn(sessionDto);

        // Use ObjectMapper to convert the DTO to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Session Create"))
                .andExpect(jsonPath("$.teacher_id").value(1))
                .andExpect(jsonPath("$.description").value("Yoga Session Description"));
    }




    @WithMockUser(username="yoga@studio.com")
    @Test
    void updateSessionTest_Valid() throws Exception {
        Session session = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .build();

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga session updated");
        sessionDto.setDescription("Description updated");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDate(new Date());

        Session updatedSession = Session.builder()
                .id(1L)
                .name(sessionDto.getName())
                .date(sessionDto.getDate())
                .description(sessionDto.getDescription())
                .build();

        given(sessionService.getById(session.getId())).willReturn(session);
        given(sessionMapper.toEntity(sessionDto)).willReturn(updatedSession);
        given(sessionService.update(session.getId(), updatedSession)).willReturn(updatedSession);
        given(sessionMapper.toDto(updatedSession)).willReturn(sessionDto);

        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        mockMvc.perform(put("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga session updated"))
                .andExpect(jsonPath("$.description").value("Description updated"));
    }



    @WithMockUser(username="yoga@studio.com")
    @Test
    void updateSessionTest_SessionDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/session/999"))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username="yoga@studio.com")
    @Test
    void deleteSessionTest_Valid() throws Exception {
        Session session = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .build();


        given(sessionService.getById(session.getId())).willReturn(session);
        willDoNothing().given(sessionService).delete(session.getId());

        mockMvc.perform(delete("/api/session/" + session.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService).delete(session.getId());
    }


    @WithMockUser(username="yoga@studio.com")
    @Test
    void deleteSessionTest_SessionDoesNotExist() throws Exception {
        mockMvc.perform(delete("/api/session/999"))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username="yoga@studio.com")
    @Test
    void participateSessionTest_Valid() throws Exception {
        Session session = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(new ArrayList<>())
                .build();

        given(sessionService.create(session)).willReturn(session);
        given(sessionService.getById(session.getId())).willReturn(session);
        willDoNothing().given(sessionService).participate(session.getId(), 1L);
        willDoNothing().given(sessionService).delete(session.getId());

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/1"))
                .andExpect(status().isOk());

        sessionService.delete(session.getId());
        verify(sessionService).delete(session.getId());
    }


    @WithMockUser(username="yoga@studio.com")
    @Test
    void participateSessionTest_SessionDoesNotExist() throws Exception {
        doThrow(new NotFoundException()).when(sessionService).participate(999L, 1L);

        mockMvc.perform(post("/api/session/999/participate/1"))
                .andExpect(status().isNotFound());
    }


    @WithMockUser(username="yoga@studio.com")
    @Test
    void participateSessionTest_UserDoesNotExist() throws Exception {
        Session session = Session.builder()
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(new ArrayList<>())
                .build();
        session.setId(1L);

        doThrow(new NotFoundException()).when(sessionService).participate(session.getId(), 25L);

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/25"))
                .andExpect(status().isNotFound());

        sessionService.delete(session.getId());
    }


    @WithMockUser(username="yoga@studio.com")
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

        List<User> userList = new ArrayList<>();
        userList.add(user);
        Session session = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(userList)
                .build();

        doThrow(new BadRequestException()).when(sessionService).participate(session.getId(), user.getId());

        mockMvc.perform(post("/api/session/" + session.getId() + "/participate/1"))
                .andExpect(status().isBadRequest());

        sessionService.delete(session.getId());
    }


    @WithMockUser(username="yoga@studio.com")
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

        List<User> userList = new ArrayList<>();
        userList.add(user);
        Session session = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(userList)
                .build();

        willDoNothing().given(sessionService).noLongerParticipate(session.getId(), user.getId());

        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/" + user.getId()))
                .andExpect(status().isOk());

        sessionService.delete(session.getId());
    }

    @WithMockUser(username="yoga@studio.com")
    @Test
    void noLongerParticipateSessionTest_UserDoesNotParticipate() throws Exception {
        Session session = Session.builder()
                .id(1L)
                .name("Yoga session")
                .date(new Date())
                .description("Description")
                .users(new ArrayList<>())
                .build();


        doThrow(new BadRequestException()).when(sessionService).noLongerParticipate(session.getId(), 1L);

        mockMvc.perform(delete("/api/session/" + session.getId() + "/participate/1"))
                .andExpect(status().isBadRequest());

        sessionService.delete(session.getId());
    }

    @WithMockUser(username="yoga@studio.com")
    @Test
    void noLongerParticipateSessionTest_SessionDoesNotExist() throws Exception {
        doThrow(new NotFoundException()).when(sessionService).noLongerParticipate(1L, 1L);

        mockMvc.perform(delete("/api/session/1/participate/1"))
                .andExpect(status().isNotFound());
    }

}
