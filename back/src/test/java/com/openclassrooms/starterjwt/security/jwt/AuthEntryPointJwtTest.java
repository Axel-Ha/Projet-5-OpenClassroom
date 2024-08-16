package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class AuthEntryPointJwtTest {
    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private AuthenticationException authenticationException;

    @Mock
    private ServletOutputStream outputStream;

    @Test
    void commenceTest() throws IOException, ServletException {
        when(httpServletResponse.getOutputStream()).thenReturn(outputStream);
        when(httpServletRequest.getServletPath()).thenReturn("/path");
        when(authenticationException.getMessage()).thenReturn("Unauthorized error");

        authEntryPointJwt.commence(httpServletRequest, httpServletResponse, authenticationException);

        verify(httpServletResponse).setContentType(MediaType.APPLICATION_JSON_VALUE);
        verify(httpServletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
