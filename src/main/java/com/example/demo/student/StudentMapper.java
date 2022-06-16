package com.example.demo.student;

import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public StudentDto toStudentDto(Student student) {
        Long groupId = student.getGroup() != null ? student.getGroup().getId() : null;
        return StudentDto.builder().id(student.getId()).name(student.getName()).email(student.getEmail())
                .dateOfBirth(student.getDateOfBirth()).groupId(groupId).build();
    }

    public Student toStudent(StudentDto studentDto) {
        return Student.builder().id(studentDto.getId()).name(studentDto.getName())
                .email(studentDto.getEmail()).dateOfBirth(studentDto.getDateOfBirth()).build();
    }
}
