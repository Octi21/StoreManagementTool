package com.example.StoreManagementTool.mapper;

import com.example.StoreManagementTool.dto.response.UserResponse;
import com.example.StoreManagementTool.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(AppUser user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt()
        );
    }
}