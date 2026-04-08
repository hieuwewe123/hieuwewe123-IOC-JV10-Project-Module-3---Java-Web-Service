package org.example.coursemanagementsystem.exception;

import org.example.coursemanagementsystem.dto.response.ApiResponse;
import org.example.coursemanagementsystem.dto.response.ErrorDetail;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorDetail.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Dữ liệu không hợp lệ", errors));
    }

        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(ApiResponse.error("Thông tin đăng nhập không chính xác", null));
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ApiResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(ApiResponse.error("Bạn không có quyền truy cập tài nguyên này", null));
        }

        @ExceptionHandler(ResponseStatusException.class)
        public ResponseEntity<ApiResponse<Object>> handleResponseStatus(ResponseStatusException ex) {
                HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
                return ResponseEntity.status(status)
                                .body(ApiResponse.error(ex.getReason() != null ? ex.getReason() : "Yêu cầu không hợp lệ", null));
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(ApiResponse.error("Xung đột dữ liệu nghiệp vụ", null));
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiResponse<Object>> handleIllegalArgument(IllegalArgumentException ex) {
                HttpStatus status = mapStatusByMessage(ex.getMessage());
                return ResponseEntity.status(status)
                                .body(ApiResponse.error(ex.getMessage(), null));
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ApiResponse<Object>> handleRuntimeException(RuntimeException ex) {
                HttpStatus status = mapStatusByMessage(ex.getMessage());
                return ResponseEntity.status(status)
                                .body(ApiResponse.error(ex.getMessage(), null));
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(ApiResponse.error("Lỗi hệ thống: " + ex.getMessage(), null));
    }

        private HttpStatus mapStatusByMessage(String message) {
                if (message == null || message.isBlank()) {
                        return HttpStatus.BAD_REQUEST;
                }

                String lower = message.toLowerCase();
                if (lower.contains("không tồn tại") || lower.contains("not found")) {
                        return HttpStatus.NOT_FOUND;
                }
                if (lower.contains("không có quyền") || lower.contains("forbidden") || lower.contains("truy cập")) {
                        return HttpStatus.FORBIDDEN;
                }
                if (lower.contains("đã tồn tại") || lower.contains("đã đăng ký") || lower.contains("trùng") || lower.contains("xung đột")) {
                        return HttpStatus.CONFLICT;
                }
                return HttpStatus.BAD_REQUEST;
        }
}
