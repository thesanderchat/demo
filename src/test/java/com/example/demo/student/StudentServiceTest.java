package com.example.demo.student;

import com.example.demo.group.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    private StudentRepository mockStudentRepository;
    private StudentMapper mockStudentMapper;
    private StudentService testee;

    @BeforeEach
    void setup() {
        mockStudentRepository = mock(StudentRepository.class);
        mockStudentMapper = mock(StudentMapper.class);
        testee = new StudentService(mockStudentRepository, mockStudentMapper);
    }

    @Test
    void getStudents() {
        List<Student> students = List.of(Student.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build());

        when(mockStudentRepository.findAllByOrderByIdAsc()).thenReturn(students);

        testee.getStudents();

        verify(mockStudentRepository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    void getStudentById() {
        Student student = Student.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        StudentDto studentDto = StudentDto.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();

        when(mockStudentRepository.findById(student.getId())).thenReturn(Optional.of(student));
        when(mockStudentMapper.toStudentDto(student)).thenReturn(studentDto);

        testee.getStudentById(student.getId());
    }


    @Test
    void getStudentById_WhenStudentWithIdDoesntExist() {
        Long studentId = 3L;

        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.empty());
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.getStudentById(studentId));

        assertEquals("No student with id " + studentId, illegalStateException.getMessage());
    }

    @Test
    void addNewStudent_WhenStudentDontExistInDb() {
        Student student = Student.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        StudentDto studentDto = StudentDto.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();

        when(mockStudentMapper.toStudent(studentDto)).thenReturn(student);

        testee.addNewStudent(studentDto);

        verify(mockStudentRepository, times(1)).save(student);
    }

    @Test
    void deleteStudent_WhenStudentDontExistsInDb_ThenThrowIllegalStateException() {
        Long studentId = 3L;

        when(mockStudentRepository.existsById(studentId)).thenReturn(false);
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.deleteStudent(studentId));

        assertEquals("student with id " + studentId + " does not exists", illegalStateException.getMessage());

    }


    @Test
    void deleteStudent_WhenStudentExistsInDb_ThenVerifyDeleting() {
        Long studentId = 1L;
        Student student = Student.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();

        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.of(student));

        testee.deleteStudent(studentId);

        verify(mockStudentRepository, times(1)).deleteById(studentId);
    }


    @Test
    void deleteStudent_WhenStudentExistsInGroup_ThenThrowIllegalStateException() {
        Long studentId = 1L;
        Group group = Group.builder().id(1L).name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();
        Student student = Student.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).group(group).build();

        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.of(student));

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.deleteStudent(studentId));

        assertEquals("student " + studentId + " in group with id " + student.getGroup().getId(), illegalStateException.getMessage());
    }

    @Test
    void updateStudent_WhenStudentDontExistsInDb_ThenThrowIllegalStateException() {
        Long studentId = 3L;

        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.empty());

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.updateStudent(studentId, StudentDto.builder().name("name1").email("email1")
                        .dateOfBirth(LocalDate.of(2020, 2, 18)).build()));

        assertEquals("student with id " + studentId + " does not exists", illegalStateException.getMessage());
    }

    @Test
    void updateStudent_WhenStudentExistsInDb_ThenUpdateStudentSuccessfully() {
        Student student = new Student();
        Long studentId = 1L;
        StudentDto studentDto = StudentDto.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();

        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.of(student));

        testee.updateStudent(studentId, studentDto);

        assertEquals("name1", student.getName());
        assertEquals("email1", student.getEmail());
        assertEquals(LocalDate.of(2020, 2, 18), student.getDateOfBirth());

    }
}