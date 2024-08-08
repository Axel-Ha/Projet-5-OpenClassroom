package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.openclassrooms.starterjwt.models.User;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {
    @InjectMocks
    private SessionService sessionService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testCreateSession(){
        Session mockSession = new Session();
        when(sessionRepository.save(mockSession)).thenReturn(mockSession);
        sessionService.create(mockSession);
        verify(sessionRepository).save(mockSession);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
    void delete(){
        Long sessionId = 1L;
        sessionService.delete(sessionId);
        verify(sessionRepository).deleteById(sessionId);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
    void testFindAll(){
        Session mockSession1 = new Session();
        Session mockSession2 = new Session();

        List<Session> mockSessions= new ArrayList<Session>();
        mockSessions.add(mockSession1);
        mockSessions.add(mockSession2);

        when(sessionRepository.findAll()).thenReturn(mockSessions);

        List<Session> listSessions = sessionService.findAll();

        assertNotNull(listSessions);
        assertEquals(mockSessions,listSessions);

        verify(sessionRepository).findAll();
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
    void testGetById_findExistSession(){
        Session mockSession = new Session();
        Long sessionId = 1L;
        mockSession.setId(sessionId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        Session sessionTest = sessionService.getById(sessionId);

        assertNotNull(sessionTest);
        assertEquals(mockSession.getId(),sessionTest.getId());

        verify(sessionRepository).findById(sessionId);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
    void testGetById_SessionNotExist(){
        Long sessionId = 1L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        Session sessionTest = sessionService.getById(sessionId);

        assertNull(sessionTest);
        verify(sessionRepository).findById(sessionId);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
     void testUpdateSession(){
        Session mockSession = new Session();
        Long sessionId = 1L;
        mockSession.setId(sessionId);
        mockSession.setName("Name 1");

        Session mockSessionUpdated = new Session();
        mockSessionUpdated.setId(sessionId);
        mockSessionUpdated.setName("Name updated");

        when(sessionRepository.save(mockSessionUpdated)).thenReturn(mockSessionUpdated);

        Session sessionUpdate = sessionService.update(sessionId, mockSessionUpdated);

        assertNotNull(sessionUpdate);
        assertEquals(mockSessionUpdated.getName(),sessionUpdate.getName());

        verify(sessionRepository).save(mockSessionUpdated);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
     void testParticipate_UserExist(){
        Long sessionId = 1L;
        Long userId = 1L;
        User mockUser = new User();
        Session mockSession = new Session();

        mockUser.setId(userId);
        mockSession.setId(sessionId);
        mockSession.setUsers(new ArrayList<User>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        sessionService.participate(sessionId,userId);
        assertTrue(mockSession.getUsers().contains(mockUser));

        verify(sessionRepository).save(mockSession);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
     void testParticipate_UserNotExist(){
        Long sessionId = 1L;
        Long userId = 1L;
        Session mockSession = new Session();

        mockSession.setId(sessionId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> sessionService.participate(sessionId,userId));
        verifyNoInteractions(userRepository);
    }

    @Test
     void testParticipate_SessionNotExist(){
        Long sessionId = 1L;
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> sessionService.participate(sessionId,userId));
        verifyNoInteractions(userRepository);
    }

    @Test
    void testParticipate_SessionAndUserNotExist() {
        Long sessionId = 1L;
        Long userId = 1L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));

        verify(sessionRepository).findById(sessionId);
        verify(userRepository).findById(userId); // Ensure findById on userRepository is called
        verifyNoMoreInteractions(sessionRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
     void testParticipate_UserAlreadyParticipate(){
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);

        List<User> usersList = new ArrayList<User>();
        usersList.add(mockUser);
        mockSession.setUsers(usersList);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        assertThrows(BadRequestException.class,() -> sessionService.participate(sessionId,userId));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
     void testNoLongerParticipate(){
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);

        List<User> usersList = new ArrayList<User>();
        usersList.add(mockUser);
        mockSession.setUsers(usersList);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        sessionService.noLongerParticipate(sessionId,userId);
        assertFalse(mockSession.getUsers().contains(mockUser));
        verify(sessionRepository).findById(sessionId);
        verify(sessionRepository).save(mockSession);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
     void testNoLongerParticipate_UserNotExist(){
        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, 1L));
        verifyNoInteractions(userRepository);
    }

    @Test
     void testNoLongerParticipate_SessionNotExist(){
        Long sessionId = 1L;
        Long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,()-> sessionService.participate(sessionId,userId));
        verify(sessionRepository).findById(sessionId);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
     void testNoLongerParticipate_UserNotParticipate(){
        Long notParticipateUserId = 1L;
        User mockUser = new User();
        mockUser.setId(2L);

        Long sessionId = 1L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);

        List<User> usersList = new ArrayList<User>();
        usersList.add(mockUser);
        mockSession.setUsers(usersList);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        assertThrows(BadRequestException.class, ()-> sessionService.noLongerParticipate(sessionId,notParticipateUserId));
        verifyNoInteractions(userRepository);
    }
}
