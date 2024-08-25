package com.openclassrooms.starterjwt.Controllers.Integrations;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeacherControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherMapper teacherMapper;

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
    public void findTeacherByIdTest_valid() throws URISyntaxException {
        Teacher teacher = new Teacher();
        teacher.setFirstName("Jean");
        teacher.setLastName("LAST");
        teacherRepository.save(teacher);

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/teacher/" + teacher.getId();
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<TeacherDto> result = this.restTemplate.exchange(uri, HttpMethod.GET, request, TeacherDto.class);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(teacher.getId(), result.getBody().getId());

        teacherRepository.delete(teacher);
    }

    @Test
    public void findTeacherByIdTest_TeacherNotFound() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/api/teacher/999";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Void> result = this.restTemplate.exchange(uri, HttpMethod.GET, request, Void.class);

        assertEquals(404, result.getStatusCodeValue());
    }

    @Test
    public void findAllTeacherTest() throws URISyntaxException {
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("Jean");
        teacher1.setLastName("LAST");

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jean");
        teacher2.setLastName("LAST");
        teacherRepository.saveAll(Arrays.asList(teacher1, teacher2));

        final String baseUrl = "http://localhost:" + randomServerPort + "/api/teacher";
        URI uri = new URI(baseUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<List> result = this.restTemplate.exchange(uri, HttpMethod.GET, request, List.class);

        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody().size() > 0);

        teacherRepository.deleteAll(Arrays.asList(teacher1, teacher2));
    }
}
