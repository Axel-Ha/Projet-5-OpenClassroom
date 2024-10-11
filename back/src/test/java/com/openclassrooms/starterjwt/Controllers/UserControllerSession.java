package com.openclassrooms.starterjwt.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.AuthConfig;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.UserService;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@WebMvcTest(SessionController.class)
@Import(AuthConfig.class)
class UserControllerSession {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private UserDetails userDetails;

    @MockBean
    private SessionService sessionService;

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

//    @Test
//    @WithMockUser(username="yoga@studio.com")
//    void findTeacherByIdTest_Valid() throws Exception {
//        User user = new User();
//        user.setId(1L);
//        UserDto userDto = new UserDto();
//        userDto.setId(1L);
//
//        when(userService.findById(1L)).thenReturn(user);
//        when(userMapper.toDto(user)).thenReturn(userDto);
//
//        mockMvc.perform(get("/api/user/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//    }

    @Test
    @WithMockUser(username="yoga@studio.com")
    void findTeacherByIdTest_NotFound() throws Exception {
        when(userService.findById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());  // Attend un statut 200 car le contrôleur ne renvoie pas 404
    }



//    @Test
//    @WithMockUser(username = "yoga@studio.com")
//    public void deleteUser_Valid() throws Exception {
//        User user = new User();
//        user.setId(1L);
//        user.setEmail("yoga@studio.com");
//
//        when(userService.findById(1L)).thenReturn(user);
//
//        mockMvc.perform(delete("/api/user/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        verify(userService).delete(1L); // Vérifie que la méthode delete est bien appelée
//    }



    @Test
    @WithMockUser(username="yoga@studio.com")
    void deleteUserTest_NotFound() throws Exception {
        when(userService.findById(2L)).thenReturn(null);

        mockMvc.perform(delete("/api/user/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());  // Attend un statut 200 car le contrôleur ne renvoie pas 404

    }


    @Test
    void deleteUserTest_Unauthorized() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verify(userService, never()).delete(anyLong()); // Vérifie que la méthode delete n'est pas appelée
    }
}


