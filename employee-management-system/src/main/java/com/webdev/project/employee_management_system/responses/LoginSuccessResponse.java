package com.webdev.project.employee_management_system.responses;

import com.webdev.project.employee_management_system.dtos.UserDTO;

public class LoginSuccessResponse {
    private String token;

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private UserDTO user;

    public LoginSuccessResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }

    // getters and setters
}
