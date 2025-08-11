package com.hana7.hanaro.cart.dto;

import jakarta.validation.constraints.Min;

public record CartUpdateRequestDTO(
	@Min(1)
	int cnt
) {
}
