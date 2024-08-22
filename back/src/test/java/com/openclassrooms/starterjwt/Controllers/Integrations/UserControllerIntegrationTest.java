package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.AuthConfig;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(AuthConfig.class)
class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;


    @WithMockUser(username="yoga@studio.com")
    @Test
    void findUserByIdTest_valid() throws Exception {
        User user = new User();
        user.setId(1L);

        UserDto userDto = new UserDto();
        userDto.setId(1L);

        given(userService.findById(1L)).willReturn(user);
        given(userMapper.toDto(user)).willReturn(userDto);

        mockMvc.perform(get("/api/user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }


    @Test
    @WithMockUser(username="yoga@studio.com")
    void findUserByIdTest_UserNotFound() throws Exception {
        mockMvc.perform(get("/api/user/55"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@gmail.com")
    void deleteUserTest_Valid() throws Exception {
        // Create a user object with the same email as the authenticated user.
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        // Mock the behavior of userService to return this user.
        given(userService.findById(1L)).willReturn(user);

        // Perform the delete request.
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());

    }

    @Test
    void deleteUserTest_UnauthorizedToDeleteUser() throws Exception {
        // Create a user object with the same email as the authenticated user.
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        // Mock the behavior of userService to return this user.
        given(userService.findById(1L)).willReturn(user);

        // Perform the delete request.
        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username="yoga@studio.com")
    void deleteUserTest_UserNotExist() throws Exception {
        mockMvc.perform(delete("/api/user/55"))
                .andExpect(status().isNotFound());
    }

}
