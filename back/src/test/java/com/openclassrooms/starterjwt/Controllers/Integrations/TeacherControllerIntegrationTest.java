package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

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
    void findTeacherByIdTest_valid() throws Exception {
        mockMvc.perform(get("/api/teacher/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void findTeacherByIdTest_TeacherNotFound() throws Exception {
        mockMvc.perform(get("/api/teacher/55")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllTeacherTest() throws Exception{
        mockMvc.perform(get("/api/teacher")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[1].id").value("2"));
    }
}
