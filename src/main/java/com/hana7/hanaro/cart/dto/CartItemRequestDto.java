package com.hana7.hanaro.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequestDto {
    private Long itemId;
    private int quantity;
}
