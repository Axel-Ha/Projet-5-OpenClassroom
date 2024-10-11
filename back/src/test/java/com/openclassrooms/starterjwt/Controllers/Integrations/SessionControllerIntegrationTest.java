package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.AuthConfig;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(AuthConfig.class)
public class SessionControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private SessionMapper sessionMapper;


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
    public void findSessionByIdTest_ExistSession() throws URISyntaxException {
        Session session = new Session();
        session.setName("Yoga session");
        session.setDescription("A relaxing yoga session");
        session.setDate(new Date());
        sessionRepository.save(session);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/" + session.getId();
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<SessionDto> result = this.restTemplate.exchange(uri, HttpMethod.GET, request, SessionDto.class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(session.getId(), result.getBody().getId());
        assertEquals("Yoga session", result.getBody().getName());

        sessionRepository.delete(session);
    }



    @Test
    public void findSessionByIdTest_SessionDoesNotExist() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/999";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.GET, request, Void.class);

        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void findAllSessionTest() throws URISyntaxException {
        Session session = new Session();
        session.setName("Yoga session");
        session.setDescription("A relaxing yoga session");
        session.setDate(new Date());
        sessionRepository.save(session);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<List> result = this.restTemplate.exchange(uri, HttpMethod.GET, request, List.class);

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(!result.getBody().isEmpty());

        sessionRepository.delete(session);
    }

    @Test
    public void createSessionTest() throws URISyntaxException, JsonProcessingException {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga Session Create");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("Yoga Session Description");
        sessionDto.setDate(new Date());

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session";
        URI uri = new URI(baseUrl);

        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(sessionDtoJson, headers);

        ResponseEntity<SessionDto> result = this.restTemplate.postForEntity(uri, request, SessionDto.class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Yoga Session Create", result.getBody().getName());

        sessionRepository.deleteById(result.getBody().getId());
    }

    @Test
    public void updateSessionTest_Valid() throws URISyntaxException, JsonProcessingException {
        Session session = new Session();
        session.setName("Yoga session");
        session.setDescription("A relaxing yoga session");
        session.setDate(new Date());
        sessionRepository.save(session);

        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Yoga session updated");
        sessionDto.setDescription("Description updated");
        sessionDto.setTeacher_id(1L);
        sessionDto.setDate(new Date());

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/" + session.getId();
        URI uri = new URI(baseUrl);

        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(sessionDtoJson, headers);

        ResponseEntity<SessionDto> result = this.restTemplate.exchange(uri, HttpMethod.PUT, request, SessionDto.class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Yoga session updated", result.getBody().getName());

        sessionRepository.delete(session);
    }

    @Test
    public void updateSessionTest_SessionDoesNotExist() throws URISyntaxException, JsonProcessingException {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Non-existing session");

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/999";
        URI uri = new URI(baseUrl);

        ObjectMapper objectMapper = new ObjectMapper();
        String sessionDtoJson = objectMapper.writeValueAsString(sessionDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(sessionDtoJson, headers);

        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.PUT, request, Void.class);

        assertEquals(400, result.getStatusCodeValue());
    }

    @Test
    public void deleteSessionTest_Valid() throws URISyntaxException {
        Session session = new Session();
        session.setName("Yoga session");
        session.setDescription("A relaxing yoga session");
        session.setDate(new Date());
        sessionRepository.save(session);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/" + session.getId();
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, Void.class);

        assertEquals(200, result.getStatusCodeValue());

        sessionRepository.delete(session);
    }

    @Test
    public void deleteSessionTest_SessionDoesNotExist() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/999";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, Void.class);

        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void participateSessionTest_Valid() throws URISyntaxException {
        Session session = new Session();
        session.setName("Yoga session");
        session.setDescription("A relaxing yoga session");
        session.setDate(new Date());
        sessionRepository.save(session);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/" + session.getId() + "/participate/1";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.postForEntity(uri, request, Void.class);

        assertEquals(200, result.getStatusCodeValue());

        sessionRepository.delete(session);
    }

    @Test
    public void participateSessionTest_SessionDoesNotExist() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/999/participate/1";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.postForEntity(uri, request, Void.class);

        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void noLongerParticipateSessionTest_Valid() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/1/participate/1";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, Void.class);

        assertEquals(200, result.getStatusCodeValue());

        ResponseEntity<Void> reParticipateResult = this.restTemplate.postForEntity(uri, request, Void.class);

        assertEquals(200, reParticipateResult.getStatusCodeValue());

    }

    @Test
    public void noLongerParticipateSessionTest_SessionDoesNotExist() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/session/999/participate/1";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.DELETE, request, Void.class);

        assertEquals(404, result.getStatusCodeValue());
    }
}
