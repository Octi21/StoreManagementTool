package com.example.StoreManagementTool.dto.response;

import com.example.StoreManagementTool.entity.Role;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        Role role,
        boolean enabled,
        Instant createdAt
) {}