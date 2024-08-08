package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void testDeleteUser(){
        userService.delete(1L);

        verify(userRepository).deleteById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testFindUserById_UserExist(){
        User mockUser = new User();
        mockUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User testUser = userService.findById(1L);

        assertNotNull(testUser);
        assertEquals(testUser,mockUser);
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testFindTeacherById_TeacherNotExist(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User userTest = userService.findById(1L);

        assertNull(userTest);
        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);
    }
}
