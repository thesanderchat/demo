package com.example.demo.group;

import com.example.demo.student.StudentDto;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GroupDto {
    private Long id;
    @NotEmpty(message = "Name may not be empty")
    private String name;
    @NotNull(message = "Date of creation may not be null")
    private LocalDate dateOfCreation;
    @Builder.Default
    private List<StudentDto> studentList = new ArrayList<>();
}
