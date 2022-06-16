package com.example.demo.group;


import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class GroupService {
    private final GroupMapper groupMapper;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public List<GroupDto> getGroups() {
        return groupRepository.findAllByOrderByIdAsc().stream()
                .map(groupMapper::toGroupDto)
                .collect(Collectors.toList());
    }

    public GroupDto getGroupById(Long groupId) {
        return groupRepository.findById(groupId)
                .map(groupMapper::toGroupDto)
                .orElseThrow(() -> new IllegalStateException("group with id " + groupId + " does not exists"));
    }

    public void addNewGroup(GroupDto groupDto) {
        groupRepository.save(groupMapper.toGroup(groupDto));
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalStateException("group with id " + groupId + " does not exists"));
        group.removeAllStudentsFromStudentList();
        groupRepository.deleteById(groupId);
    }

    @Transactional
    public void updateGroup(Long groupId, @Valid GroupDto groupDto) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalStateException("group with id " + groupId + " does not exists"));
        group.setName(groupDto.getName());
        group.setDateOfCreation(groupDto.getDateOfCreation());
    }

    @Transactional
    public void addStudentToGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalStateException("group with id " + groupId + " does not exists"));
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new IllegalStateException("student with id " + studentId + " does not exists"));
        group.addNewStudentToStudentList(student);
    }

    @Transactional
    public void removeStudentFromGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new IllegalStateException("group with id " + groupId + " does not exists"));
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new IllegalStateException("student with id " + studentId + " does not exists"));
        if (student.getGroup() == group) {
            group.removeStudentFromStudentList(student);
        } else {
            throw new IllegalStateException("student with id " + studentId + " not in group with id " + groupId);
        }
    }
}
