package com.example.demo.student;

import com.example.demo.group.Group;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentMapperTest {
    private final StudentMapper testee = new StudentMapper();

    @Test
    void toStudentDto() {
        Group group = Group.builder().name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();
        Student student = Student.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).group(group).build();
        StudentDto studentDto = testee.toStudentDto(student);

        assertEquals("name1", studentDto.getName());
        assertEquals("email1", studentDto.getEmail());
        assertEquals(LocalDate.of(2020, 2, 18), studentDto.getDateOfBirth());
        assertEquals(student.getGroup().getId(), studentDto.getGroupId());
    }

    @Test
    void toStudentDto_WhenNoGroup() {
        Student student = Student.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        StudentDto studentDto = testee.toStudentDto(student);

        assertEquals("name1", studentDto.getName());
        assertEquals("email1", studentDto.getEmail());
        assertEquals(LocalDate.of(2020, 2, 18), studentDto.getDateOfBirth());
        assertEquals(student.getGroup(), studentDto.getGroupId());
    }

    @Test
    void toStudent() {
        StudentDto studentDto = StudentDto.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        Student student = testee.toStudent(studentDto);
        assertEquals("name1", student.getName());
        assertEquals("email1", student.getEmail());
        assertEquals(LocalDate.of(2020, 2, 18), studentDto.getDateOfBirth());
    }
}