package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class SessionTest {

    @Test
    void sessionGettersSettersAndBuilderTest() {
        Long id = 1L;
        String sessionName = "Session Name";
        Date date = new Date();
        String description = "description session.";
        Teacher teacher = new Teacher();
        LocalDateTime now = LocalDateTime.now();

        Session session = Session.builder()
                .id(id)
                .name(sessionName)
                .date(date)
                .description(description)
                .teacher(teacher)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(id, session.getId());
        assertEquals(sessionName, session.getName());
        assertEquals(date, session.getDate());
        assertEquals(description, session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());

        String newSessionName = "New Session Name";
        session.setName(newSessionName);
        assertEquals(newSessionName, session.getName());
    }

    @Test
    void sessionEqualsAndHashCodeTest() {
        Session session1 = Session.builder().id(1L).build();
        Session session2 = Session.builder().id(1L).build();
        Session session3 = Session.builder().id(2L).build();

        assertEquals(session1, session2);
        assertNotEquals(session1, session3);
        assertEquals(session1.hashCode(), session2.hashCode());
        assertNotEquals(session1.hashCode(), session3.hashCode());
    }

    @Test
    void sessionToStringTest() {
        Session session = Session.builder().id(1L).name("Session Name").build();
        String expected = "Session(id=1, name=Session Name, date=null, description=null, teacher=null, users=null, createdAt=null, updatedAt=null)";
        assertEquals(expected, session.toString());
    }
}
