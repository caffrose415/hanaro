package com.hana7.hanaro.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "C001", "잘못된 요청입니다."),
	JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "C002", "요청 본문을 해석할 수 없습니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다."),
	ACCESS_DENIED(HttpStatus.FORBIDDEN, "A002", "접근 권한이 없습니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "C404", "리소스를 찾을 수 없습니다."),
	DATA_INTEGRITY(HttpStatus.CONFLICT, "D001", "데이터 무결성 오류입니다."),
	PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "F001", "업로드 용량을 초과했습니다."),
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 오류입니다.");

	private final HttpStatus status;
	private final String code;
	private final String defaultMessage;

	ErrorCode(HttpStatus status, String code, String defaultMessage) {
		this.status = status; this.code = code; this.defaultMessage = defaultMessage;
	}
}
