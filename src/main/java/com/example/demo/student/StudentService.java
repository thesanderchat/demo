package com.example.demo.student;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public List<StudentDto> getStudents() {
        return studentRepository.findAllByOrderByIdAsc().stream()
                .map(studentMapper::toStudentDto)
                .collect(Collectors.toList());
    }

    public StudentDto getStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .map(studentMapper::toStudentDto)
                .orElseThrow(() -> new IllegalStateException("No student with id " + studentId));
    }

    public void addNewStudent(StudentDto studentDto) {
        Student student = studentMapper.toStudent(studentDto);
        studentRepository.save(student);

    }

    public void deleteStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new IllegalStateException("student with id " + studentId + " does not exists"));
        if (student.getGroup() == null) {
            studentRepository.deleteById(studentId);
        } else {
            throw new IllegalStateException("student " + studentId + " in group with id " + student.getGroup().getId());
        }
    }

    @Transactional
    public void updateStudent(Long studentId, StudentDto studentDto) {
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new IllegalStateException("student with id " + studentId + " does not exists"));
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setDateOfBirth(studentDto.getDateOfBirth());
    }
}
