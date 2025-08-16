package com.webdev.project.employee_management_system.responses;

import com.webdev.project.employee_management_system.utils.ErrorDetail;

public class ErrorResponse {
    private boolean success = false;
    private ErrorDetail error;

    public ErrorResponse(String code, String message) {
        this.error = new ErrorDetail(code, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public ErrorDetail getError() {
        return error;
    }
}
