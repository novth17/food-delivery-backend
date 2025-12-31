package com.lilyhien.requestDto;

import com.lilyhien.model.Cart;
import com.lilyhien.model.Food;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.List;

@Data
public class AddCartItemRequest {

    private long foodId;
    private int quantity;
    private List<String> ingredients;
}
