package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class JwtUtilsTest {

    @InjectMocks
    JwtUtils jwtUtils;

    @Mock
    UserDetailsImpl userDetails;

    @Mock
    Authentication authentication;

    @Mock
    Logger logger;

    private String jwtSecret = "JwtSecret";
    private int jwtExpiration = 3600000;
    private String global_username = "test@gmail.com";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field jwtSecretPrivateField = JwtUtils.class.getDeclaredField("jwtSecret");
        jwtSecretPrivateField.setAccessible(true);
        jwtSecretPrivateField.set(jwtUtils, "JwtSecret");

        Field jwtExpirationPrivateField = JwtUtils.class.getDeclaredField("jwtExpirationMs");
        jwtExpirationPrivateField.setAccessible(true);
        jwtExpirationPrivateField.set(jwtUtils, 3600000);
    }

    @Test
    void generateTokenTest() {
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(global_username);

        String token = jwtUtils.generateJwtToken(authentication);
        assertNotNull(token);

        verify(authentication).getPrincipal();
        verify(userDetails).getUsername();
        verifyNoMoreInteractions(authentication, userDetails);
    }


    @Test
    void getUserNameFromJwtTokenTest() {
        String username = global_username;
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String extractedUsername = jwtUtils.getUserNameFromJwtToken(token);

        assertEquals(username, extractedUsername);
    }


    @Test
    public void ValidateJwtTokenTest_ValidToken() {
        String token = Jwts.builder()
                .setSubject(global_username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertTrue(isValid);
    }

    @Test
    public void ValidateJwtTokenTest_InvalidSignature() {
        String token = Jwts.builder()
                .setSubject(global_username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, "invalidSecretKey")
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    public void ValidateJwtTokenTest_TokenInvalidFormat() {
        String token = "Invalid Format token";

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    public void ValidateJwtTokenTest_TokenExpired() {
        String token = Jwts.builder()
                .setSubject(global_username)
                .setIssuedAt(new Date(System.currentTimeMillis() - jwtExpiration - 1000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    public void ValidateJwtTokenTest_UnsupportedToken() {
        String token = Jwts.builder()
                .setSubject(global_username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .compact();

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }

    @Test
    public void ValidateJwtTokenTest_EmptyJwt() {
        String token = "";

        boolean isValid = jwtUtils.validateJwtToken(token);

        assertFalse(isValid);
    }
}
