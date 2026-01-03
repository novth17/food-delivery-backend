package com.lilyhien.requestDto;

import com.lilyhien.model.Address;
import lombok.Data;

@Data
public class OrderRequest {
    private Long RestaurantId;
    private Address deliveryAddress;
}
