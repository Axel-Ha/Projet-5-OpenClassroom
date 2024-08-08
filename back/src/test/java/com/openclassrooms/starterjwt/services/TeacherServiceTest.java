package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @InjectMocks
    TeacherService teacherService;

    @Mock
    TeacherRepository teacherRepository;

    @Test
    void testFindAll(){
        Teacher mockTeacher1 = new Teacher();
        Teacher mockTeacher2 = new Teacher();
        List<Teacher> mockTeachers = new ArrayList<Teacher>();

        mockTeachers.add(mockTeacher1);
        mockTeachers.add(mockTeacher2);

        when(teacherRepository.findAll()).thenReturn(mockTeachers);

        List<Teacher> listTeachers = teacherRepository.findAll();

        assertNotNull(listTeachers);
        assertEquals(listTeachers,mockTeachers);

        verify(teacherRepository).findAll();
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
    void testFindTeacherById_TeacherExist(){
        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(mockTeacher));

        Teacher teacherTest = teacherService.findById(1L);

        assertNotNull(teacherTest);
        assertEquals(teacherTest,mockTeacher);

        verify(teacherRepository).findById(1L);
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
    void testFindTeacherById_TeacherNotExist(){
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        Teacher teacherTest = teacherService.findById(1L);

        assertNull(teacherTest);
        verify(teacherRepository).findById(1L);
        verifyNoMoreInteractions(teacherRepository);
    }
}
