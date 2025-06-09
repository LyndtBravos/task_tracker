package com.sgs.tasktracker.service;

import com.sgs.tasktracker.dto.UserRequestDTO;
import com.sgs.tasktracker.dto.UserResponseDTO;
import com.sgs.tasktracker.dto.UserTaskDTO;
import com.sgs.tasktracker.entity.User;

import java.util.List;

public interface UserService {

    UserResponseDTO toDto(User user);
    UserResponseDTO createUser(UserRequestDTO user);
    UserResponseDTO getUserById(Integer id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Integer id, UserRequestDTO user);
    void deleteUser(Integer id);
    UserTaskDTO getUserWithTasks(Integer userId);

}