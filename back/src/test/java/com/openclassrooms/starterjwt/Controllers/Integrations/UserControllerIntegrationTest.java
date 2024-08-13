package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    public void setupAuthAndToken(){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("yoga@studio.com","test!1234")
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    void findUserByIdTest_valid() throws Exception {
        mockMvc.perform(get("/api/user/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findUserByIdTest_UserNotFound() throws Exception {
        mockMvc.perform(get("/api/user/55")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUserTest_Valid() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("TANNER");
        signupRequest.setFirstName("Jean");
        signupRequest.setPassword("test1234");
        signupRequest.setEmail("test@gmail.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)));


        User user = userRepository.findByEmail(signupRequest.getEmail()).orElseThrow(null);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken("test@gmail.com","test1234")
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        token = jwtUtils.generateJwtToken(authentication);

        mockMvc.perform(delete("/api/user/"+user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUserTest_UnauthorizedToDeleteUser() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("TANNER");
        signupRequest.setFirstName("Jean");
        signupRequest.setPassword("test1234");
        signupRequest.setEmail("test@gmail.com");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)));

        User user = userRepository.findByEmail(signupRequest.getEmail()).orElseThrow(null);

        mockMvc.perform(delete("/api/user/"+user.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteUserTest_UserNotExist() throws Exception {
        mockMvc.perform(delete("/api/user/55")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }



}
