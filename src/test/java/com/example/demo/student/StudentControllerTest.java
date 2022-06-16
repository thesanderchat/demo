package com.example.demo.student;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentService mockStudentService;


    @Test
    void getStudents_WhenGot_ThenCheckStatusAndContent() throws Exception {
        List<StudentDto> result = List.of(StudentDto.builder().name("name1").email("email1").dateOfBirth(LocalDate.of(2020, 2, 18)).build());
        when(mockStudentService.getStudents()).thenReturn(result);
        this.mockMvc.perform(get("/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(toJsonString(result)));
    }

    @Test
    void getStudentById_WhenGot_ThenCheckStatusAndContent() throws Exception {
        Long studentId = 1L;
        StudentDto result = StudentDto.builder().name("name1").email("email1").dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        when(mockStudentService.getStudentById(studentId)).thenReturn(result);
        this.mockMvc.perform(get("/students/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(toJsonString(result)));
    }

    @Test
    void registerNewStudent_WhenDone_ThenCheckIsItCreated() throws Exception {
        StudentDto studentDto = StudentDto.builder().name("name1").email("email1").dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(toJsonString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void registerNewStudent_WhenValidationFailed_ThenReturnBadStatus() throws Exception {
        StudentDto studentDto = StudentDto.builder().name("").email("email1").dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(toJsonString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteStudent_WhenDone_ThenCheckIsItDeleted() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateStudent_WhenValidationFailed_ThenReturnBadStatus() throws Exception {
        StudentDto studentDto = StudentDto.builder().name("").email("email1").dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/students/1")
                        .content(toJsonString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStudent_WhenValidationOk_ThenCheckIsItCreated() throws Exception {
        StudentDto studentDto = StudentDto.builder().name("name1").email("email1").dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/students/1")
                        .content(toJsonString(studentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    private String toJsonString(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper.writeValueAsString(object);
    }
}
