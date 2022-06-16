package com.example.demo.group;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/groups")
@AllArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @GetMapping
    public List<GroupDto> getGroups() {
        return groupService.getGroups();
    }

    @GetMapping(path = "{groupId}")
    public GroupDto getGroupById(@PathVariable("groupId") Long groupId) {
        return groupService.getGroupById(groupId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerNewGroup(@RequestBody @Valid GroupDto groupDto) {
        groupService.addNewGroup(groupDto);
    }

    @DeleteMapping(path = "{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGroup(@PathVariable("groupId") Long groupId) {
        groupService.deleteGroup(groupId);
    }

    @PutMapping(path = "{groupId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void updateGroup(@PathVariable("groupId") Long groupId,
                            @RequestBody @Valid GroupDto groupDto) {
        groupService.updateGroup(groupId, groupDto);
    }

    @PutMapping(path = "{groupId}/{studentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addStudentToGroup(@PathVariable("groupId") Long groupId,
                                  @PathVariable("studentId") Long studentId) {
        groupService.addStudentToGroup(groupId, studentId);
    }

    @DeleteMapping(path = "{groupId}/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeStudentFromGroup(@PathVariable("groupId") Long groupId,
                                       @PathVariable("studentId") Long studentId) {
        groupService.removeStudentFromGroup(groupId, studentId);
    }
}
