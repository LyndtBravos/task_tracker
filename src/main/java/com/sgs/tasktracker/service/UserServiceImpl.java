package com.sgs.tasktracker.service;

import com.sgs.tasktracker.dto.TaskResponseDTO;
import com.sgs.tasktracker.dto.UserRequestDTO;
import com.sgs.tasktracker.dto.UserResponseDTO;
import com.sgs.tasktracker.dto.UserTaskDTO;
import com.sgs.tasktracker.entity.User;
import com.sgs.tasktracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private TaskServiceImpl taskService;

    public UserResponseDTO toDto(User user) {
        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole().name());
        dto.setPassword(user.getPassword().replaceAll("\\W|\\w", "*"));

        return dto;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        System.out.println(">>> createUser() - Hitting DB");
        User user = new User(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(),
                new HashSet<>(), User.Role.valueOf(userDTO.getRole()));

        repo.save(user);
        return toDto(user);
    }

    @Override
    @Cacheable(value = "users")
    public ArrayList<UserResponseDTO> getAllUsers() {
        System.out.println(">>> getAllUsers() - Hitting DB");
        return new ArrayList<>(repo.findAll()
                .stream()
                .map(this::toDto)
                .toList());
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public UserResponseDTO getUserById(Integer id) {
        System.out.println(">>> getUserById(" + id + ") - Hitting DB");
        User user = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Please retry. User not found."));
        return toDto(user);
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    public UserResponseDTO updateUser(Integer id, UserRequestDTO updatedUser) {
        System.out.println(">>> updateUser(" + id + ") - Hitting DB");
        User user = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Update logic failed, user not found."));

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setRole(User.Role.valueOf(updatedUser.getRole()));
        user.setPassword(updatedUser.getPassword());

        repo.save(user);
        return toDto(user);
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    public void deleteUser(Integer id) {
        System.out.println(">>> deleteUser(" + id + ") - Hitting DB");
        if (!repo.existsById(id))
            throw new EntityNotFoundException("This user(id: %d) doesn't exist".formatted(id));

        repo.deleteById(id);
    }

    @Override
    @Cacheable(value = "userTasks", key = "#userId")
    public UserTaskDTO getUserWithTasks(Integer userId) {
        System.out.println(">>> getUserWithTasks(" + userId + ") - Hitting DB");
        UserTaskDTO dto = new UserTaskDTO();

        User user = repo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: %d, doesn't exist.".formatted(userId)));

        List<TaskResponseDTO> tasks = user.getTasks()
                .stream()
                .map(taskService::toDto)
                .sorted(Comparator.comparing(TaskResponseDTO::getId))
                .toList();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setTasks(tasks);
        dto.setRole(user.getRole().name());

        return dto;
    }
}
