package com.openclassrooms.starterjwt.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.AuthConfig;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(SessionController.class)
@Import(AuthConfig.class)
class SessionControllerTest {
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

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username="yoga@studio.com")
    void findSessionByIdTest_valid() throws Exception {
        // Arrange: Simuler les données Session et SessionDto
        Session session = new Session();
        session.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        // Simuler le comportement des services
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        // Act & Assert: Simuler l'appel HTTP GET et vérifier la réponse
        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        // Vérification des interactions
        verify(sessionService).getById(1L);
        verify(sessionMapper).toDto(session);
        verifyNoMoreInteractions(sessionService, sessionMapper);
    }


    @Test
    @WithMockUser(username="yoga@studio.com")
    void findSessionByIdTest_NotFound() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(1L);
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    @WithMockUser(username="yoga@studio.com")
    void FindAllSessionTest_Valid() throws Exception {
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        ArrayList<Session> sessionArrayList = new ArrayList<>();
        sessionArrayList.add(session);

        ArrayList<SessionDto> sessionDtoArrayList = new ArrayList<>();
        sessionDtoArrayList.add(sessionDto);

        when(sessionService.findAll()).thenReturn(sessionArrayList);
        when(sessionMapper.toDto(sessionArrayList)).thenReturn(sessionDtoArrayList);

        mockMvc.perform(get("/api/session")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));

        verify(sessionService).findAll();
        verify(sessionMapper).toDto(sessionArrayList);
        verifyNoMoreInteractions(sessionService, sessionMapper);
    }

    @Test
    @WithMockUser(username="yoga@studio.com")
    void createSessionTest() throws Exception {
        Session session = new Session();
        session.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga session");
        sessionDto.setId(1L);
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Session description");

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).create(session);
        verify(sessionMapper).toDto(session);
        verifyNoMoreInteractions(sessionMapper, sessionService);
    }



    @Test
    @WithMockUser(username="yoga@studio.com")
    void updateSessionTest_valid() throws Exception {
        Session session = new Session();
        session.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga session");
        sessionDto.setId(1L);
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Session description");

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).update(1L, session);
        verify(sessionMapper).toDto(session);
        verifyNoMoreInteractions(sessionMapper, sessionService);
    }


    @Test
    @WithMockUser(username="yoga@studio.com")
    void updateSessionTest_NotFound() throws Exception {
        Session session = new Session();
        session.setId(1L);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga session");
        sessionDto.setId(1L);
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Session description");

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(2L, session)).thenReturn(null);

        mockMvc.perform(put("/api/session/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk());

        verify(sessionMapper).toEntity(sessionDto);
        verify(sessionService).update(2L, session);
    }

    @Test
    @WithMockUser(username="yoga@studio.com")
    void deleteSessionTest() throws Exception {
        Session session = new Session();
        session.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);

        mockMvc.perform(delete("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService).delete(1L);
        verify(sessionService).getById(1L);
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    @WithMockUser(username="yoga@studio.com")
    void deleteSessionTest_NotFound() throws Exception {
        when(sessionService.getById(2L)).thenReturn(null);

        mockMvc.perform(delete("/api/session/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(sessionService).getById(2L);
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    @WithMockUser(username="yoga@studio.com")
    void participateToSessionTest() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService).participate(1L, 1L);
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    @WithMockUser(username="yoga@studio.com")
    void noLongerParticipateToSessionTest() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(sessionService).noLongerParticipate(1L, 1L);
        verifyNoMoreInteractions(sessionService);
    }


}
