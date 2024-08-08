package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class AuthTokenFilterTest {

    @InjectMocks
    AuthTokenFilter authTokenFilter;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    HttpServletResponse httpServletResponse;

    @Mock
    FilterChain filterChain;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    UserDetails userDetails;

    @Test
    void doFilterInternalTest_ValidToken() throws ServletException, IOException {
        String mockJwt = "mockJwt";
        String mockUsername = "mockEmail@gmail.com";

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + mockJwt);
        when(jwtUtils.validateJwtToken(mockJwt)).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(mockJwt)).thenReturn(mockUsername);
        when(userDetailsService.loadUserByUsername(mockUsername)).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(new ArrayList<>());

        authTokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

        verify(jwtUtils).validateJwtToken(mockJwt);
        verify(jwtUtils).getUserNameFromJwtToken(mockJwt);
        verify(userDetailsService).loadUserByUsername(mockUsername);
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);

        assertEquals(authenticationToken, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternalTest_InvalidToken() throws ServletException, IOException {
        String mockJwt = "mockJwt";

        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + mockJwt);
        when(jwtUtils.validateJwtToken(mockJwt)).thenReturn(false);

        authTokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletRequest).getHeader("Authorization");
        verify(jwtUtils).validateJwtToken(mockJwt);
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);

        verifyNoMoreInteractions(httpServletRequest, httpServletResponse, jwtUtils, userDetailsService, userDetails, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternalTest_NoToken() throws ServletException, IOException {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        authTokenFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletRequest).getHeader("Authorization");
        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);

        verifyNoMoreInteractions(httpServletRequest, httpServletResponse, jwtUtils, userDetailsService, userDetails, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
