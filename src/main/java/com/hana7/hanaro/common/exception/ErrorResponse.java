package com.hana7.hanaro.common.exception;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ErrorResponse")
public record ErrorResponse(
	@Schema(example = "2025-08-12T10:22:33.123Z") Instant timestamp,
	@Schema(example = "/user/cart/items") String path,
	@Schema(example = "POST") String method,
	@Schema(example = "A001") String code,
	@Schema(example = "인증이 필요합니다.") String message,
	@Schema(description = "필드 검증 오류 목록") List<FieldError> errors,
	@Schema(description = "추가 정보") Map<String,Object> detail
) {
	@Schema(name = "FieldError")
	public record FieldError(String field, String reason) {
	}
}

