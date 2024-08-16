package com.openclassrooms.starterjwt.Controllers;

import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionService;

    @Test
    void findSessionByIdTest_valid(){
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> responseEntity = sessionController.findById("1");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(sessionDto, responseEntity.getBody());
    }

    @Test
    void findSessionByIdTest_NotFound(){
        when(sessionService.getById(1L)).thenReturn(null);
        ResponseEntity<?> responseEntity = sessionController.findById("1");
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void FindAllSessionTest_Valid(){
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        ArrayList<Session> sessionArrayList = new ArrayList<Session>();
        sessionArrayList.add(session);

        ArrayList<SessionDto> sessionDtoArrayList = new ArrayList<SessionDto>();
        sessionDtoArrayList.add(sessionDto);

        when(sessionService.findAll()).thenReturn(sessionArrayList);
        when(sessionMapper.toDto(sessionArrayList)).thenReturn(sessionDtoArrayList);

        ResponseEntity<?> responseEntity = sessionController.findAll();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(sessionDtoArrayList, responseEntity.getBody());
    }

    @Test
    void createSessionTest(){
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void updateSessionTest_valid(){
        Session session = new Session();
        session.setId(1L);
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(1L);

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(1L,session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update("1",sessionDto);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void updateSessionTest_NotFound(){
        Session session = new Session();
        session.setId(1L);

        when(sessionService.update(2L,session)).thenReturn(null);

        ResponseEntity<?> responseEntity = sessionController.findById("2");
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void deleteSessionTest(){
        Session session = new Session();
        session.setId(1L);

        when(sessionService.getById(1L)).thenReturn(session);

        ResponseEntity<?> responseEntity = sessionController.save("1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(sessionService).delete(1L);
        verify(sessionService).getById(1L);
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    void deleteSessionTest_NotFound(){
        when(sessionService.getById(2L)).thenReturn(null);

        ResponseEntity<?> responseEntity = sessionController.save("2");
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void partcipateToSessionTest(){
        ResponseEntity<?> responseEntity = sessionController.participate("1","1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(sessionService).participate(1L,1L);
        verifyNoMoreInteractions(sessionService);
    }

    @Test
    void noLongerParticipateToSessionTest(){
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("1","1");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(sessionService).noLongerParticipate(1L,1L);
        verifyNoMoreInteractions(sessionService);
    }

}
