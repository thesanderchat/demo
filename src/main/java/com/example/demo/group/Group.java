package com.example.demo.group;

import com.example.demo.student.Student;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Group {
    @Id
    @SequenceGenerator(
            name = "group_sequence",
            sequenceName = "group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "group_sequence"
    )
    private Long id;
    @NotEmpty(message = "Name may not be empty")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "Date of creation may not be null")
    @Column(nullable = false)
    private LocalDate dateOfCreation;
    @Builder.Default
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER)
    private List<Student> studentList = new ArrayList<>();

    public void addNewStudentToStudentList(Student student) {
        studentList.add(student);
        student.setGroup(this);
    }

    public void removeStudentFromStudentList(Student student) {
        studentList.remove(student);
        student.setGroup(null);
    }

    public void removeAllStudentsFromStudentList() {
        for (Student student : this.studentList) {
            student.setGroup(null);
        }
        studentList.clear();
    }
}
