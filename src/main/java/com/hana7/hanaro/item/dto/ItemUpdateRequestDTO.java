package com.hana7.hanaro.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public record ItemUpdateRequestDTO (
    long id,

    @NotBlank(message = "상품명은 필수입니다.")
    @Schema(name="name", example = "updateItem")
    String name,

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    @NotBlank(message = "가격을 입력해주세요")
    @Schema(name="price", example = "10000")
    int price,

    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    @NotBlank(message="재고를 입력해주세요")
    @Schema(name="stock", example = "10")
    int stock
){}
