package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class UserTest {

    @Test
    void userGettersSettersAndBuilderTest() {
        Long id = 1L;
        String firstName = "Jean";
        String lastName = "TANNER";
        String email = "test@gmail.com";
        String password = "test123";
        LocalDateTime now = LocalDateTime.now();

        User user = User.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .admin(true)
                .password(password)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(id, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
        assertTrue(user.isAdmin());

        String newTeacherName = "Joe";
        user.setFirstName(newTeacherName);
        assertEquals(newTeacherName, user.getFirstName());
    }

    @Test
    void equalsAndHashCodeTest() {
        Session user1 = Session.builder().id(1L).build();
        Session user2 = Session.builder().id(1L).build();
        Session user3 = Session.builder().id(2L).build();

        assertEquals(user1, user2);
        assertNotEquals(user2, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    void toStringTest() {
        User user = User.builder().id(1L).firstName("Jean").lastName("TANNER").password("test").email("test@gmail.com").admin(true).build();
        String expected = "User(id=1, email=test@gmail.com, lastName=TANNER, firstName=Jean, password=test, admin=true, createdAt=null, updatedAt=null)";
        assertEquals(expected, user.toString());
    }
}
