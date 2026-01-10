package com.lilyhien.requestDto;

import com.lilyhien.model.Category;
import com.lilyhien.model.IngredientsItem;
import com.lilyhien.model.Restaurant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateFoodRequest {

    @NotBlank(message = "Food name is required!")
    private String name;

    @NotBlank(message = "Food description is required!")
    private String description;

    @NotNull(message = "Food price is required!")
    @Min(value = 0, message = "Price cannot be negative")
    private Long price;

    private Category foodCategory;
    private List<String> images;
    private boolean isAvailable;
    private Long restaurantId;
    private boolean isVegetarian;
    private boolean isSeasonal;
    private List<IngredientsItem> ingredients;
}
