package com.lilyhien.service;

import com.lilyhien.model.IngredientsCategory;
import com.lilyhien.model.IngredientsItem;

import java.util.List;

//handle IngredientsCategory and IngredientsItem in the same service, but they are in face 2 different entities
public interface IngredientsService {

    IngredientsCategory createIngredientCategory(String name,  Long userId) throws Exception;

    IngredientsCategory findIngredientCategoryById(Long id) throws Exception;
    List<IngredientsCategory> findIngredientCategoryByRestaurantUserId(Long userId) throws Exception;
    IngredientsItem createIngredientsItem(Long userId, String ingredientName, Long ingredientCategoryId) throws Exception;

    List<IngredientsItem> findRestaurantsIngredients(Long userId) throws Exception;

    IngredientsItem updateStock(Long ingredientId, Long userId) throws Exception;
}

