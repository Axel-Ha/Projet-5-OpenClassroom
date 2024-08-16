package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void authenticateAdminUserTest_Valid() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(loginRequest.getEmail()))
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    void authenticateNonAdminUserTest_Valid() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("TANNER");
        signupRequest.setFirstName("Jean");
        signupRequest.setPassword("test1234");
        signupRequest.setEmail("test@gmail.com");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());


        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("test1234");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(loginRequest.getEmail()))
                .andExpect(jsonPath("$.admin").value(false));

        User user = userRepository.findByEmail("test@gmail.com").orElseThrow(null);
        userService.delete(user.getId());
    }

    @Test
    void authenticateAdminUserTest_Failed() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("emailNotExisting@gmail.com");  // Using incorrect email to simulate failure
        loginRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
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

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        User user = userRepository.findByEmail("test@gmail.com").orElseThrow(null);
        userService.delete(user.getId());
    }

    @Test
    void registerUser_EmailAlreadyTaken() throws Exception{
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("TANNER");
        signupRequest.setFirstName("Jean");
        signupRequest.setPassword("test1234");
        signupRequest.setEmail("yoga@studio.com");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already taken!"));

    }

}
