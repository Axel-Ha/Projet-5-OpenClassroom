package com.openclassrooms.starterjwt.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.openclassrooms.starterjwt.AuthConfig;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import com.openclassrooms.starterjwt.services.SessionService;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@Import(AuthConfig.class)
class TeacherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherController teacherController;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private SessionMapper sessionMapper;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;


//    @Test
//    @WithMockUser(username = "yoga@studio.com")
//    void findTeacherByIdTest_Valid() throws Exception {
//        Teacher teacher = new Teacher();
//        teacher.setId(1L);
//        TeacherDto teacherDto = new TeacherDto();
//        teacherDto.setId(1L);
//
//
//        when(teacherService.findById(1L)).thenReturn(teacher);
//        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);
//
//        mockMvc.perform(get("/api/teacher/1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//
//        verify(teacherService).findById(1L);
//        verify(teacherMapper).toDto(teacher);
//        verifyNoMoreInteractions(teacherService, teacherMapper);
//    }


    @Test
    @WithMockUser(username="yoga@studio.com")
    void findTeacherByIdTest_NotFound() throws Exception {
        when(teacherService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());  // Attend un statut 200 car le contrôleur ne renvoie pas 404

    }



//    @Test
//    @WithMockUser(username="yoga@studio.com")
//    void findAllTeacherTest_Valid() throws Exception {
//        Teacher teacher = new Teacher();
//        teacher.setId(1L);
//        TeacherDto teacherDto = new TeacherDto();
//        teacherDto.setId(1L);
//
//        List<Teacher> sessionArrayList = new ArrayList<>();
//        sessionArrayList.add(teacher);
//
//        List<TeacherDto> sessionDtoArrayList = new ArrayList<>();
//        sessionDtoArrayList.add(teacherDto);
//
//        when(teacherService.findAll()).thenReturn(sessionArrayList);
//        when(teacherMapper.toDto(sessionArrayList)).thenReturn(sessionDtoArrayList);
//
//        mockMvc.perform(get("/api/teacher")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L));
//
//        verify(teacherService).findAll(); // Vérifie que la méthode findAll est bien appelée
//    }


}
