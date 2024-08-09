package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class TeacherMapperTest {

    @InjectMocks
    TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);;

    @Test
    void toDtoTest_ValidTeacher(){
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .lastName("TANNER")
                .firstName("Jean")
                .createdAt(now)
                .updatedAt(now)
                .build();

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertEquals(teacher.getId(),teacherDto.getId());
        assertEquals(teacher.getLastName(),teacherDto.getLastName());
        assertEquals(teacher.getFirstName(),teacherDto.getFirstName());
        assertEquals(teacher.getUpdatedAt(),teacherDto.getUpdatedAt());
        assertEquals(teacher.getUpdatedAt(),teacherDto.getUpdatedAt());
    }

    @Test
    void toDtoTest_TeacherNull(){
        Teacher teacher = null;
        TeacherDto teacherDto = teacherMapper.toDto(teacher);
        assertNull(teacherDto);
    }

    @Test
    void toEntityTest_ValidTeacher(){
        LocalDateTime now = LocalDateTime.now();
        TeacherDto teacherDto = new TeacherDto(1L,"TANNER","Jean",now,now);

        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }

    @Test
    void toEntityTest_TeacherNull(){
        TeacherDto teacherDto = null;
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        assertNull(teacher);
    }

}
