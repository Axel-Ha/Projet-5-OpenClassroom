package com.openclassrooms.starterjwt.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.AuthConfig;
import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.AuthEntryPointJwt;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(AuthConfig.class)
//création de AuthConfig car AuthEntryPointJwt n'est pas appelé alors qu'une erreur est relevé
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private UserService userService;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void authenticateUserTest() throws Exception {
        LoginRequest mockLoginRequest = new LoginRequest();
        mockLoginRequest.setEmail("test@email.com");
        mockLoginRequest.setPassword("test1234");

        LocalDateTime now = LocalDateTime.now();

        User mockUser = User.builder()
                .id(1L)
                .email("test@email.com")
                .firstName("Jean")
                .lastName("TANNER")
                .admin(true)
                .updatedAt(now)
                .createdAt(now)
                .password("test1234")
                .build();

        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@email.com", "Jean", "TANNER", true, "test1234");
        when(mockAuthentication.getPrincipal()).thenReturn(userDetails);

        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(mockUser));

        when(jwtUtils.generateJwtToken(mockAuthentication)).thenReturn("Token");

        String loginRequestJson = objectMapper.writeValueAsString(mockLoginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("Token"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("test@email.com"))
                .andExpect(jsonPath("$.admin").value(true));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(mockAuthentication);
        verify(userRepository).findByEmail(userDetails.getUsername());
        verifyNoMoreInteractions(authenticationManager, jwtUtils, userRepository);
    }

    @Test
    void registerUserTest_Valid() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("Jean");
        signupRequest.setLastName("TANNER");
        signupRequest.setEmail("test@gmail.com");
        signupRequest.setPassword("test1234");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);

        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("test1234");

        String signupRequestJson = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    void registerUserTest_EmailAlreadyTaken() throws Exception {
        // Arrange: Créer l'objet SignupRequest
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("Jean");
        signupRequest.setLastName("TANNER");
        signupRequest.setEmail("test@gmail.com");
        signupRequest.setPassword("test1234");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        String signupRequestJson = objectMapper.writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }
}


