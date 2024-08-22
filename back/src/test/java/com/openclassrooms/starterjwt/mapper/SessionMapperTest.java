package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SessionMapperTest {
    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Test
    void toDtoTest_SessionNotNull(){
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        List<User> users = new ArrayList<User>();

        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(date)
                .description("Yoga Session Description")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        SessionDto SessionDto = sessionMapper.toDto(session);

        assertEquals(session.getId(), SessionDto.getId());
        assertEquals(session.getName(), SessionDto.getName());
        assertEquals(session.getDate(), SessionDto.getDate());
        assertEquals(session.getDescription(), SessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), SessionDto.getTeacher_id());
        assertEquals(session.getUsers(), SessionDto.getUsers());
        assertEquals(session.getCreatedAt(), SessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(), SessionDto.getUpdatedAt());

    }

    @Test
    void toDtoTest_SessionNull(){
        Session session = null;
        SessionDto sessionDto = sessionMapper.toDto(session);
        assertNull(sessionDto);
    }

    @Test
    void toEntityTest_SessionNotNull(){
        LocalDateTime now = LocalDateTime.now();
        Date date = new Date();
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        List<Long> users = new ArrayList<>();
        users.add(1L);
        users.add(2L);
        String description = "description";
        SessionDto sessionDto = new SessionDto(1L,
                "Session Dto",
                date,
                teacher.getId(),
                description,
                users,
                now,
                now);
        when(teacherService.findById(1L)).thenReturn(teacher);

        Session sessionToEntity= sessionMapper.toEntity(sessionDto);

        assertEquals(sessionDto.getId(), sessionToEntity.getId());
        assertEquals(sessionDto.getName(), sessionToEntity.getName());
        assertEquals(sessionDto.getDate(), sessionToEntity.getDate());
        assertEquals(sessionDto.getTeacher_id(), sessionToEntity.getTeacher().getId());
        assertEquals(sessionDto.getDescription(), sessionToEntity.getDescription());
        assertEquals(sessionDto.getUsers().size(), sessionToEntity.getUsers().size());
        assertEquals(sessionDto.getCreatedAt(), sessionToEntity.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), sessionToEntity.getUpdatedAt());
    }

    @Test
    void toEntityTest_SessionNull(){
        SessionDto sessionDto = null;
        Session sessionEntity = sessionMapper.toEntity(sessionDto);
        assertNull(sessionEntity);
    }
}
