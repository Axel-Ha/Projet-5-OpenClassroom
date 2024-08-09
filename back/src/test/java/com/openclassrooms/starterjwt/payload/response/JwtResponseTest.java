package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class JwtResponseTest {

    @Test
    void jwtResponseConstructorAndGettersTest() {
        String token = "testToken";
        Long id = 1L;
        String username = "test@gmail.com";
        String firstName = "Jean";
        String lastName = "TANNER";
        Boolean admin = true;

        JwtResponse jwtResponse = new JwtResponse(token, id, username, firstName, lastName, admin);

        assertEquals(token, jwtResponse.getToken());
        assertEquals("Bearer", jwtResponse.getType());
        assertEquals(id, jwtResponse.getId());
        assertEquals(username, jwtResponse.getUsername());
        assertEquals(firstName, jwtResponse.getFirstName());
        assertEquals(lastName, jwtResponse.getLastName());
        assertTrue(jwtResponse.getAdmin());
    }

    @Test
    void jwtResponseSettersTest() {
        JwtResponse jwtResponse = new JwtResponse(null, null, null, null, null, null);

        jwtResponse.setToken("testToken");
        jwtResponse.setId(2L);
        jwtResponse.setUsername("test@gmail.com");
        jwtResponse.setFirstName("Jean");
        jwtResponse.setLastName("TANNER");
        jwtResponse.setAdmin(false);

        assertEquals("testToken", jwtResponse.getToken());
        assertEquals(2L, jwtResponse.getId());
        assertEquals("test@gmail.com", jwtResponse.getUsername());
        assertEquals("Jean", jwtResponse.getFirstName());
        assertEquals("TANNER", jwtResponse.getLastName());
        assertFalse(jwtResponse.getAdmin());
    }
}
