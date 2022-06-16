package com.example.demo.group;

import com.example.demo.student.Student;
import com.example.demo.student.StudentDto;
import com.example.demo.student.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceTest {
    private GroupRepository mockGroupRepository;
    private StudentRepository mockStudentRepository;
    private GroupMapper mockGroupMapper;
    private GroupService testee;

    @BeforeEach
    void setup() {
        mockGroupRepository = mock(GroupRepository.class);
        mockStudentRepository = mock(StudentRepository.class);
        mockGroupMapper = mock(GroupMapper.class);
        testee = new GroupService(mockGroupMapper, mockStudentRepository, mockGroupRepository);
    }

    @Test
    void getGroups_WhenWeHaveGroups_ThenUpdateGroupSuccessfully() {
        Group group = Group.builder().id(1L).name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();
        List<Group> groups = List.of(group);
        GroupDto groupDto = GroupDto.builder().id(1L).name("name1")
                .dateOfCreation(LocalDate.of(2020, 2, 18)).build();
        List<GroupDto> groupDtos = List.of(groupDto);

        when(mockGroupRepository.findAllByOrderByIdAsc()).thenReturn(groups);
        when(mockGroupMapper.toGroupDto(group)).thenReturn(groupDto);
        List<GroupDto> groups1 = testee.getGroups();

        assertEquals(groupDtos, groups1);
    }

    @Test
    void getGroupById_ThenWeHaveGroupWithRightId_ThenVerifyFinding() {
        Group group = Group.builder().id(1L).name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();
        GroupDto groupDto = GroupDto.builder().id(1L).name("name1")
                .dateOfCreation(LocalDate.of(2020, 2, 18)).build();

        when(mockGroupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(mockGroupMapper.toGroupDto(group)).thenReturn(groupDto);

        GroupDto group1 = testee.getGroupById(group.getId());
        assertEquals(group1, groupDto);
    }

    @Test
    void getGroupById_ThenWeDontHaveGroupWithRightId_ThenThrowIllegalStateException() {
        Group group = Group.builder().id(1L).name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();
        Long groupId = group.getId();

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.getGroupById(groupId));

        assertEquals("group with id " + groupId + " does not exists", illegalStateException.getMessage());
    }

    @Test
    void addNewGroup_WhenAllGood_ThenVerifyAdding() {
        Group group = new Group();
        List<StudentDto> students = List.of(StudentDto.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build());
        GroupDto groupDto = GroupDto.builder().id(1L).name("name1")
                .dateOfCreation(LocalDate.of(2020, 2, 18)).studentList(students).build();

        when(mockGroupMapper.toGroup(groupDto)).thenReturn(group);

        testee.addNewGroup(groupDto);

        verify(mockGroupRepository, times(1)).save(group);
    }

    @Test
    void deleteGroup_WhenGroupExistsInDb_ThenVerifyDeleting() {
        Long groupId = 1L;
        Group group = new Group();

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        testee.deleteGroup(groupId);

        verify(mockGroupRepository, times(1)).deleteById(groupId);
    }

    @Test
    void deleteGroup_WhenGroupDontExistsInDb_ThenThrowIllegalStateException() {
        Long groupId = 3L;

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.deleteGroup(groupId));

        assertEquals("group with id " + groupId + " does not exists", illegalStateException.getMessage());
    }

    @Test
    void updateGroup_WhenGroupExistsInDb_ThenUpdateGroupSuccessfully() {
        Long groupId = 1L;
        Group group = new Group();
        List<StudentDto> students = List.of(StudentDto.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build());
        GroupDto groupDto = GroupDto.builder().id(1L).name("name1")
                .dateOfCreation(LocalDate.of(2020, 2, 18)).studentList(students).build();
        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));

        testee.updateGroup(groupId, groupDto);

        assertEquals("name1", group.getName());
        assertEquals(LocalDate.of(2020, 2, 18), group.getDateOfCreation());
    }

    @Test
    void updateGroup_WhenGroupDontExistsInDb_ThenThrowIllegalStateException() {
        Long groupId = 3L;
        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.empty());

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.updateGroup(groupId, GroupDto.builder().id(1L).name("name1")
                        .dateOfCreation(LocalDate.of(2020, 2, 18)).build()));

        assertEquals("group with id " + groupId + " does not exists", illegalStateException.getMessage());
    }

    @Test
    void addStudentToGroup_WhenGroupAndStudentExistsInDb_ThenAddStudentSuccessfully() {
        Long groupId = 4L;
        Long studentId = 5L;
        Group group = Group.builder().id(1L).name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();
        Student student = Student.builder().id(1L).name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.of(student));

        testee.addStudentToGroup(groupId, studentId);
        assertEquals(group.getId(), student.getGroup().getId());
        assertEquals(group.getStudentList(), List.of(student));
    }

    @Test
    void addStudentToGroup_WhenGroupDoesNotExistsInDb_ThenThrowIllegalStateException() {
        Long groupId = 4L;
        Long studentId = 5L;
        Student student = Student.builder().id(1L).name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.empty());
        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.of(student));

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.addStudentToGroup(groupId, studentId));
        assertEquals("group with id " + groupId + " does not exists", illegalStateException.getMessage());
    }

    @Test
    void addStudentToGroup_WhenStudentDoesNotExistsInDb_ThenThrowIllegalStateException() {
        Long groupId = 4L;
        Long studentId = 5L;
        Group group = Group.builder().id(1L).name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.empty());

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.addStudentToGroup(groupId, studentId));
        assertEquals("student with id " + studentId + " does not exists", illegalStateException.getMessage());
    }

    @Test
    void removeStudentFromGroup_WhenStudentDoesNotExistsInDb_ThenThrowIllegalStateException() {
        Long groupId = 4L;
        Long studentId = 5L;
        Group group = Group.builder().id(1L).name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.empty());

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.removeStudentFromGroup(groupId, studentId));
        assertEquals("student with id " + studentId + " does not exists", illegalStateException.getMessage());
    }

    @Test
    void removeStudentFromGroup_WhenGroupDoesNotExistsInDb_ThenThrowIllegalStateException() {
        Long groupId = 4L;
        Long studentId = 5L;
        Student student = Student.builder().id(1L).name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();

        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.empty());
        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.of(student));

        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.removeStudentFromGroup(groupId, studentId));
        assertEquals("group with id " + groupId + " does not exists", illegalStateException.getMessage());
    }

    @Test
    void removeStudentFromGroup_WhenGroupAndStudentExistsInDb_ThenAddStudentSuccessfully() {
        Long groupId = 4L;
        Long studentId = 5L;
        Student student = Student.builder().id(studentId).name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        Group group = Group.builder().id(groupId).name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();
        group.addNewStudentToStudentList(student);
        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.of(student));
        testee.removeStudentFromGroup(groupId, studentId);
        assertNull(student.getGroup());
        assertEquals(group.getStudentList(), List.of());
    }

    @Test
    void removeStudentFromGroup_WhenStudentNotInGroupAndExistsInDb_ThenThrowIllegalStateException() {
        Long groupId = 4L;
        Long studentId = 5L;
        Group group = Group.builder().id(1L).name("groupName1")
                .dateOfCreation(LocalDate.of(2021, 2, 18)).build();
        Student student = Student.builder().name("name1").email("email1")
                .dateOfBirth(LocalDate.of(2020, 2, 18)).build();
        when(mockGroupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(mockStudentRepository.findById(studentId)).thenReturn(Optional.of(student));
        IllegalStateException illegalStateException = assertThrows(IllegalStateException.class,
                () -> testee.removeStudentFromGroup(groupId, studentId));
        assertEquals("student with id " + studentId + " not in group with id " + groupId, illegalStateException.getMessage());
    }
}