package com.hana7.hanaro.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartUpdateRequestDTO(
	@Min(1)
	@NotNull
	int cnt
) {
}
