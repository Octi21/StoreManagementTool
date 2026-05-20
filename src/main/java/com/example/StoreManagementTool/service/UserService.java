package com.example.StoreManagementTool.service;

import com.example.StoreManagementTool.dto.request.CreateUserRequest;
import com.example.StoreManagementTool.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> findAll();
    UserResponse create(CreateUserRequest request);
    void delete(Long id);
}