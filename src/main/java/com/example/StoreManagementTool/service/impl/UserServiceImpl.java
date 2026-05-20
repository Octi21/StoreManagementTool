package com.example.StoreManagementTool.service.impl;

import com.example.StoreManagementTool.dto.request.CreateUserRequest;
import com.example.StoreManagementTool.dto.response.UserResponse;
import com.example.StoreManagementTool.entity.AppUser;
import com.example.StoreManagementTool.exception.DuplicateResourceException;
import com.example.StoreManagementTool.exception.ResourceNotFoundException;
import com.example.StoreManagementTool.mapper.UserMapper;
import com.example.StoreManagementTool.repository.UserRepository;
import com.example.StoreManagementTool.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> findAll(){
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }
    public UserResponse create(CreateUserRequest request){
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username '" + request.username() + "' already exists");
        }
        AppUser user = new AppUser(
                request.username(),
                passwordEncoder.encode(request.password()),  // hash here
                request.role()
        );
        AppUser saved = userRepository.save(user);
        log.info("Created user id={} username={} role={}", saved.getId(), saved.getUsername(), saved.getRole());
        return userMapper.toResponse(saved);
    }
    public void delete(Long id){
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException.of("User", id);
        }
        userRepository.deleteById(id);
        log.info("Deleted user id={}", id);
    }

}
