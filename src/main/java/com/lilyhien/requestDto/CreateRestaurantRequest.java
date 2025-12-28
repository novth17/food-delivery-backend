package com.lilyhien.requestDto;

import com.lilyhien.model.Address;
import com.lilyhien.model.ContactInformation;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

//CreateRestaurantRequestDto matches the "Create Restaurant" form on the frontend.
@Data
public class CreateRestaurantRequest {
    private Long id;

    @NotBlank (message = "Restaurant name is required!")
    private String name;
    @NotBlank (message = "Restaurant description is required!")
    private String description;
    @NotBlank (message = "Restaurant cuisine type is required!")
    private String cuisineType;
    private Address address;
    private ContactInformation contactInformation;
    private String openingHours;
    private List<String> images;
}
