package com.lilyhien.service;

import com.lilyhien.model.Category;

import java.util.List;

public interface CategoryService {

    //use user id for find restaurant for which you want to create category
    public Category createCategory(String name, Long userId) throws Exception;
    public List<Category> findCategoryByRestaurantId(Long restaurantId) throws Exception;
    public Category findCategoryById(Long categoryId) throws Exception;
}
