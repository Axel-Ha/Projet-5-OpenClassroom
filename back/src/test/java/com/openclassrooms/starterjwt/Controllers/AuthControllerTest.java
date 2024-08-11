package com.openclassrooms.starterjwt.Controllers;

import com.openclassrooms.starterjwt.controllers.AuthController;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthControllerTest {
    @InjectMocks
    AuthController authController;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;


    @Test
    void authenticateUserTest(){
        // Mocking the login request
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

        // Mocking the authentication process
        Authentication mockAuthentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuthentication);

        // Mocking user details
        UserDetailsImpl userDetails = new UserDetailsImpl(1L,"test@gmail.com","Jean","TANNER",true,"test1234");
        when(mockAuthentication.getPrincipal()).thenReturn(userDetails);

        // Mocking User Repository
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(mockUser));

        // Mocking Security Context Holder
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        // Mocking Jwt generation
        when(jwtUtils.generateJwtToken(mockAuthentication)).thenReturn("Token");

        ResponseEntity<?> responseEntity = authController.authenticateUser(mockLoginRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        JwtResponse jwtResponse = (JwtResponse) responseEntity.getBody();
        assertNotNull(jwtResponse);
        assertEquals("Token",jwtResponse.getToken());
        assertEquals(1L,jwtResponse.getId());
        assertEquals("test@gmail.com",jwtResponse.getUsername());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils).generateJwtToken(mockAuthentication);
        verify(userRepository).findByEmail(userDetails.getUsername());
        verifyNoMoreInteractions(authenticationManager,jwtUtils,userRepository);
    }

    @Test
    void registerUserTest_Valid(){
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("Jean");
        signupRequest.setLastName("TANNER");
        signupRequest.setEmail("test@gmail.com");
        signupRequest.setPassword("test1234");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("test1234Encoded");

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertNotNull(messageResponse);
        assertEquals("User registered successfully!", messageResponse.getMessage());


        User user = new User(
                signupRequest.getEmail(),
                signupRequest.getLastName(),
                signupRequest.getFirstName(),
                signupRequest.getPassword(),
                false
        );
        verify(userRepository).save(user);
        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verifyNoMoreInteractions(passwordEncoder,userRepository);
    }

    @Test
    void registerUserTest_EmailAlreadyTaken(){
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("Jean");
        signupRequest.setLastName("TANNER");
        signupRequest.setEmail("test@gmail.com");
        signupRequest.setPassword("test1234");

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        ResponseEntity<?> responseEntity = authController.registerUser(signupRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertNotNull(messageResponse);
        assertEquals("Error: Email is already taken!", messageResponse.getMessage());

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }
}


