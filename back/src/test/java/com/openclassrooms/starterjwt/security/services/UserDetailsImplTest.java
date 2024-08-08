package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {

    @InjectMocks
    private UserDetailsImpl userDetails;

    @Test
    void getAuthoritiesTest(){
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertTrue(authorities instanceof HashSet);
        assertTrue(authorities.isEmpty());
    }

    @Test
    void isAccountNonExpiredTest(){
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLockedTest(){
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpiredTest(){
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabledTest(){
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void equalsTest(){
        UserDetailsImpl userDetail1 = UserDetailsImpl
                .builder()
                .id(1L)
                .admin(true)
                .firstName("Jean")
                .lastName("Tanner")
                .username("JT")
                .password("Test123")
                .build();

        UserDetailsImpl userDetail2 = UserDetailsImpl
                .builder()
                .id(2L)
                .admin(false)
                .firstName("dea")
                .lastName("Tanner")
                .username("JT")
                .password("Test123")
                .build();

        UserDetailsImpl userDetail3 = UserDetailsImpl
                .builder()
                .id(1L)
                .admin(false)
                .firstName("dea")
                .lastName("Tanner")
                .username("JT")
                .password("Test123")
                .build();

        UserDetailsImpl userDetail4 = null;

        User user = User
                .builder()
                .id(1L)
                .admin(false)
                .email("test@test.com")
                .firstName("Test")
                .lastName("TEST")
                .password("test123")
                .build();

        assertTrue(userDetail1.getAdmin());
        assertFalse(userDetail2.getAdmin());
        assertEquals(userDetail1,userDetail3);
        assertNotEquals(userDetail1,userDetail2);
        assertNotEquals(userDetail1,userDetail4);
        assertNotEquals(userDetail1,user);

    }

}
