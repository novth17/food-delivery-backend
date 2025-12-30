package com.lilyhien.requestDto;

import lombok.Data;

@Data
public class CreateIngredientRequest {
    private String name;
    private Long categoryId;
}
