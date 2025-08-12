package com.hana7.hanaro.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex, HttpServletRequest req) {
        var code = ex.getErrorCode();
        log.warn("[BUSINESS] {} - {}", code.getCode(), ex.getMessage());
        return build(req, code, ex.getMessage(), null, null);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuth(AuthenticationException ex, HttpServletRequest req) {
        log.warn("[AUTH] {}", ex.getMessage());
        return build(req, ErrorCode.UNAUTHORIZED, ErrorCode.UNAUTHORIZED.getDefaultMessage(), null, null);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccess(AccessDeniedException ex, HttpServletRequest req) {
        log.warn("[ACCESS] {}", ex.getMessage());
        return build(req, ErrorCode.ACCESS_DENIED, ErrorCode.ACCESS_DENIED.getDefaultMessage(), null, null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldError> list = toFieldErrors(ex.getBindingResult());
        return build(req, ErrorCode.INVALID_INPUT, ErrorCode.INVALID_INPUT.getDefaultMessage(), list, null);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraint(jakarta.validation.ConstraintViolationException ex, HttpServletRequest req) {
        List<ErrorResponse.FieldError> list = ex.getConstraintViolations().stream()
            .map(v -> new ErrorResponse.FieldError(v.getPropertyPath().toString(), v.getMessage()))
            .toList();
        return build(req, ErrorCode.INVALID_INPUT, ErrorCode.INVALID_INPUT.getDefaultMessage(), list, null);
    }

    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        MethodArgumentTypeMismatchException.class,
        HttpMediaTypeNotSupportedException.class
    })
    public ResponseEntity<ErrorResponse> handleBadReq(Exception ex, HttpServletRequest req) {
        log.warn("[BAD_REQUEST] {}", ex.getMessage());
        return build(req, ErrorCode.INVALID_INPUT, ex.getMessage(), null, null);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handle404(NoHandlerFoundException ex, HttpServletRequest req) {
        return build(req, ErrorCode.NOT_FOUND, ErrorCode.NOT_FOUND.getDefaultMessage(), null, null);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handle405(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(req, ErrorCode.INVALID_INPUT, ex.getMessage(), null, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConflict(DataIntegrityViolationException ex, HttpServletRequest req) {
        log.warn("[CONFLICT] {}", ex.getMostSpecificCause().getMessage());
        return build(req, ErrorCode.DATA_INTEGRITY, ErrorCode.DATA_INTEGRITY.getDefaultMessage(), null, null);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex, HttpServletRequest req) {

        log.warn("[STATE] {}", ex.getMessage());
        return build(req, ErrorCode.INVALID_INPUT, ex.getMessage(), null, null);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleEtc(Exception ex, HttpServletRequest req) {
        log.error("[UNHANDLED] {}", ex.getMessage(), ex);
        return build(req, ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getDefaultMessage(), null, null);
    }


    private ResponseEntity<ErrorResponse> build(
        HttpServletRequest req, ErrorCode code, String message,
        List<ErrorResponse.FieldError> errors, Map<String, Object> detail
    ) {
        var body = new ErrorResponse(
            Instant.now(),
            req.getRequestURI(),
            req.getMethod(),
            code.getCode(),
            message == null ? code.getDefaultMessage() : message,
            errors == null ? List.of() : errors,
            detail == null ? Map.of() : detail
        );
        return ResponseEntity.status(code.getStatus()).body(body);
    }

    private List<ErrorResponse.FieldError> toFieldErrors(BindingResult binding) {
        List<ErrorResponse.FieldError> res = new ArrayList<>();
        for (FieldError fe : binding.getFieldErrors()) {
            res.add(new ErrorResponse.FieldError(fe.getField(), fe.getDefaultMessage()));
        }
        return res;
    }
}
