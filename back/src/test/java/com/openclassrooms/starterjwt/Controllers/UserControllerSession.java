package com.openclassrooms.starterjwt.Controllers;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserControllerSession {
    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Mock
    UserMapper userMapper;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    @Test
    void findTeacherByIdTest_Valid(){
        User user = new User();
        user.setId(1L);
        UserDto userDto = userMapper.toDto(user);

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> responseEntity = userController.findById("1");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(userDto, responseEntity.getBody());
    }

    @Test
    void findTeacherByIdTest_NotFound(){
        when(userService.findById(1L)).thenReturn(null);
        ResponseEntity<?> responseEntity = userController.findById("1");
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void deleteUser_Valid() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        when(userDetails.getUsername()).thenReturn("test@gmail.com");

        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.save("1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService).delete(1L);
    }

    @Test
    void deleteUserTest_NotFound(){
        when(userService.findById(2L)).thenReturn(null);

        ResponseEntity<?> responseEntity = userController.save("2");
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void deleteUserTest_Unauthorized(){
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        when(userDetails.getUsername()).thenReturn("testtest@gmail.com");

        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(userService.findById(1L)).thenReturn(user);

        ResponseEntity<?> response = userController.save("1");
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(userService,never()).delete(anyLong());
    }
}


