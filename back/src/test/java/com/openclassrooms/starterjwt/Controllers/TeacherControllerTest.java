package com.openclassrooms.starterjwt.Controllers;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TeacherControllerTest {
    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;


    @Test
    void findTeacherByIdTest_Valid(){
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> responseEntity = teacherController.findById("1");

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(teacherDto, responseEntity.getBody());
    }

    @Test
    void findTeacherByIdTest_NotFound(){
        when(teacherService.findById(1L)).thenReturn(null);
        ResponseEntity<?> responseEntity = teacherController.findById("1");
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void FindAllTeacherTest_Valid(){
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);

        ArrayList<Teacher> sessionArrayList = new ArrayList<Teacher>();
        sessionArrayList.add(teacher);

        ArrayList<TeacherDto> sessionDtoArrayList = new ArrayList<TeacherDto>();
        sessionDtoArrayList.add(teacherDto);

        when(teacherService.findAll()).thenReturn(sessionArrayList);
        when(teacherMapper.toDto(sessionArrayList)).thenReturn(sessionDtoArrayList);

        ResponseEntity<?> responseEntity = teacherController.findAll();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(sessionDtoArrayList, responseEntity.getBody());
    }
}
