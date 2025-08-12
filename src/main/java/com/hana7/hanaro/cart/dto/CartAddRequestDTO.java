package com.hana7.hanaro.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartAddRequestDTO (
	@NotNull
	@Schema(name = "id", example = "1")
	long id,

	@Min(1)
	@Schema(name = "cnt", example = "1")
	int cnt

){}
