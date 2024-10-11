package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    private String token;

    @BeforeEach
    public void setUp() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "yoga@studio.com","Jean","Tanner",true,"test!1234");
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext securityContext = new SecurityContextImpl(authentication);
        SecurityContextHolder.setContext(securityContext);

        token = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void findUserByIdTest_valid() throws URISyntaxException {
        User user = new User();
        user.setFirstName("Jean");
        user.setLastName("Tanner");
        user.setEmail("test@test.com");
        user.setPassword("test!1234");
        userRepository.save(user);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/user/" + user.getId();
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<UserDto> result = this.restTemplate.exchange(uri, HttpMethod.GET, request, UserDto.class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(user.getId(), result.getBody().getId());

        userRepository.delete(user);
    }

    @Test
    public void findUserByIdTest_UserNotFound() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/user/999";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.GET, request, Void.class);

        assertEquals(404, result.getStatusCodeValue());
    }

//    @Test
//    public void deleteUserTest_Valid() throws URISyntaxException {
//        User user = new User();
//        user.setFirstName("Jean");
//        user.setLastName("Tanner");
//        user.setEmail("test@test.com");
//        user.setPassword("test!1234");
//        userRepository.save(user);
//
//        final String baseUrl = "http://localhost:" + randomServerPort + "/api/user/" + user.getId();
//        URI uri = new URI(baseUrl);
//
//        // Simulate authentication as the user
//        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), true, user.getPassword());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        SecurityContext securityContext = new SecurityContextImpl(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        token = jwtUtils.generateJwtToken(authentication);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + token);
//        HttpEntity<String> request = new HttpEntity<>(headers);
//
//        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, Void.class);
//
//        assertEquals(200, result.getStatusCodeValue());
//
//        userRepository.delete(user);
//    }

    @Test
    public void deleteUserTest_UserNotExist() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/user/999";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, Void.class);

        assertEquals(404, result.getStatusCodeValue());
    }
}
