package com.hana7.hanaro.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartAddRequestDTO (
	@NotNull
	long id,

	@Min(1)
	int cnt

){}
