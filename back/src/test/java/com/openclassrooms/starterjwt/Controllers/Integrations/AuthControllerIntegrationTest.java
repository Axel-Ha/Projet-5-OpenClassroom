package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.AuthConfig;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(AuthConfig.class)
//création de AuthConfig car AuthEntryPointJwt n'est pas appelé alors qu'une erreur est relevé
class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;




    @Test
    @WithMockUser(username = "yoga@studio.com")
    void authenticateAdminUserTest_Valid() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        // Create a mock UserDetailsImpl and User
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "yoga@studio.com", "FirstName", "LastName", true, "password");
        User user = new User();
        user.setEmail("yoga@studio.com");
        user.setAdmin(true);

        // Mock the authentication process and context
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("token123");
        when(userRepository.findByEmail("yoga@studio.com")).thenReturn(Optional.of(user));

        // Convert LoginRequest to JSON
        String loginRequestJson = new ObjectMapper().writeValueAsString(loginRequest);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"))
                .andExpect(jsonPath("$.username").value("yoga@studio.com"))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    void authenticateNonAdminUserTest_Valid() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("test1234");

        // Create a mock UserDetailsImpl and User
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@gmail.com", "Jean", "TANNER", false, "password");
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setAdmin(false);

        // Mock the authentication process and context
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("token123");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(user));

        // Convert LoginRequest to JSON
        String loginRequestJson = new ObjectMapper().writeValueAsString(loginRequest);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token123"))
                .andExpect(jsonPath("$.username").value("test@gmail.com"))
                .andExpect(jsonPath("$.admin").value(false));

        // Cleanup: Delete the user after the test
        userService.delete(user.getId());
    }


    @Test
    void authenticateUserTest_Failed() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("emailNotExisting@gmail.com");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
                .thenThrow(new BadCredentialsException("Invalid credentials"));


        String loginRequestJson = new ObjectMapper().writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());
    }





    @Test
    void registerUser_Valid() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("TANNER");
        signupRequest.setFirstName("Jean");
        signupRequest.setPassword("test1234");
        signupRequest.setEmail("test@gmail.com");

        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

    }

    @Test
    void registerUser_EmailAlreadyTaken() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("TANNER");
        signupRequest.setFirstName("Jean");
        signupRequest.setPassword("test1234");
        signupRequest.setEmail("yoga@studio.com");

        when(userRepository.existsByEmail("yoga@studio.com")).thenReturn(true);

        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));
    }


}
