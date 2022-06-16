package com.example.demo;

import com.example.demo.group.Group;
import com.example.demo.group.GroupRepository;
import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class Config {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository, GroupRepository groupRepository) {
        return args -> {
            Group group1 = Group.builder().name("math").dateOfCreation(LocalDate.of(2000, Month.JULY, 18)).build();
            Group group2 = Group.builder().name("it").dateOfCreation(LocalDate.of(2000, Month.JULY, 18)).build();
            Student vladislav = Student.builder().name("Vladislav").email("pishenkovladyslav@gmail.com")
                    .dateOfBirth(LocalDate.of(2001, Month.JUNE, 16)).build();
            Student oleksandr = Student.builder().name("Oleksandr").email("sasha.dzuyniak@gmail.com")
                    .dateOfBirth(LocalDate.of(2002, Month.OCTOBER, 18)).build();
            group1.addNewStudentToStudentList(oleksandr);
            group2.addNewStudentToStudentList(vladislav);
            groupRepository.saveAll(List.of(group1,group2));
            studentRepository.saveAll(List.of(vladislav, oleksandr));
        };
    }
}
