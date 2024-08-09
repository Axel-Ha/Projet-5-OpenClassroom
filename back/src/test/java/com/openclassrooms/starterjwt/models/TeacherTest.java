package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
public class TeacherTest {

    @Test
    void teacherGettersSettersAndBuilderTest() {
        Long id = 1L;
        String firstName = "Jean";
        String lastName = "TANNER";
        LocalDateTime now = LocalDateTime.now();

        Teacher teacher = Teacher.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(id, teacher.getId());
        assertEquals(firstName, teacher.getFirstName());
        assertEquals(lastName, teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());

        String newTeacherName = "Joe";
        teacher.setFirstName(newTeacherName);
        assertEquals(newTeacherName, teacher.getFirstName());
    }

    @Test
    void equalsAndHashCodeTest() {
        Session teacher1 = Session.builder().id(1L).build();
        Session teacher2 = Session.builder().id(1L).build();
        Session teacher3 = Session.builder().id(2L).build();

        assertEquals(teacher1, teacher2);
        assertNotEquals(teacher2, teacher3);
        assertEquals(teacher1.hashCode(), teacher2.hashCode());
        assertNotEquals(teacher1.hashCode(), teacher3.hashCode());
    }

    @Test
    void toStringTest() {
        Teacher teacher = Teacher.builder().id(1L).firstName("Jean").lastName("TANNER").build();
        String expected = "Teacher(id=1, lastName=TANNER, firstName=Jean, createdAt=null, updatedAt=null)";
        assertEquals(expected, teacher.toString());
    }
}
