package com.example.demo.group;

import com.example.demo.student.StudentMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class GroupMapper {
    private final StudentMapper studentMapper;

    public GroupDto toGroupDto(Group group) {
        GroupDto groupDto = GroupDto.builder().id(group.getId()).name(group.getName()).dateOfCreation(group.getDateOfCreation()).build();
        groupDto.setStudentList(group.getStudentList().stream().map(studentMapper::toStudentDto).collect(Collectors.toList()));
        return groupDto;
    }

    public Group toGroup(GroupDto groupDto) {
        return Group.builder().id(groupDto.getId()).name(groupDto.getName()).dateOfCreation(groupDto.getDateOfCreation()).build();
    }
}
