package com.hana7.hanaro.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record StockAdjustRequestDTO(
	@NotNull
	@Schema(name="cnt", example = "10")
	int cnt
) {}
