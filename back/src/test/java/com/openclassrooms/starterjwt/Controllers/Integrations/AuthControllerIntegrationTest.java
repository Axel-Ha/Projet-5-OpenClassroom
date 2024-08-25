package com.openclassrooms.starterjwt.Controllers.Integrations;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAuthenticateAdminUserSuccess() throws URISyntaxException, JsonProcessingException {
        User user = new User();
        user.setEmail("test@studio.com");
        user.setPassword(passwordEncoder.encode("test!1234")); // Ensure the password is encoded
        user.setAdmin(true);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@studio.com");
        loginRequest.setPassword("test!1234");

        String loginRequestJson = new ObjectMapper().writeValueAsString(loginRequest);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/auth/login";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(loginRequestJson, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(true, result.getBody().contains("token"));
        assertEquals(true, result.getBody().contains("test@studio.com"));
        assertEquals(true, result.getBody().contains("admin"));

        userRepository.delete(user);
    }


    @Test
    public void testAuthenticateUserWithInvalidCredentials() throws URISyntaxException, JsonProcessingException {
        User user = new User();
        user.setEmail("yoga@studio.com");
        user.setPassword("test!1234");
        user.setAdmin(false);
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("wrongpassword");

        String loginRequestJson = new ObjectMapper().writeValueAsString(loginRequest);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/auth/login";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(loginRequestJson, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(401, result.getStatusCodeValue());

        userRepository.delete(user);
    }

    @Test
    public void registerUser_Valid() throws URISyntaxException, JsonProcessingException {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("TANNER");
        signupRequest.setFirstName("Jean");
        signupRequest.setPassword("test1234");
        signupRequest.setEmail("test@gmail.com");

        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/auth/register";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(signupRequestJson, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(true, result.getBody().contains("User registered successfully!"));

        userRepository.delete(userRepository.findByEmail("test@gmail.com").get());
    }

    @Test
    public void registerUser_EmailAlreadyTaken() throws URISyntaxException, JsonProcessingException {
        User user = new User();
        user.setEmail("test@studio.com");
        user.setPassword("test!1234");
        user.setAdmin(false);
        userRepository.save(user);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setLastName("TANNER");
        signupRequest.setFirstName("Jean");
        signupRequest.setPassword("test1234");
        signupRequest.setEmail("test@studio.com");

        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/auth/register";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(signupRequestJson, headers);

        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);

        assertEquals(400, result.getStatusCodeValue());
        assertEquals(true, result.getBody().contains("Error: Email is already taken!"));

        userRepository.delete(user);
    }
}





