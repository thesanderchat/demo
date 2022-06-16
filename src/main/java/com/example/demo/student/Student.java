package com.example.demo.student;

import com.example.demo.group.Group;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "player")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private Long id;
    @NonNull
    @Column(nullable = false)
    @NotEmpty(message = "Name may not be empty")
    private String name;
    @NonNull
    @Column(nullable = false)
    @NotEmpty(message = "Email may not be empty")
    private String email;
    @NotNull(message = "Date  may not be null")
    @NonNull
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    @ManyToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name = "player_id")
    private Group group;
}
