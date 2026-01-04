package com.lilyhien.service;

import com.lilyhien.model.Category;
import com.lilyhien.model.Restaurant;
import com.lilyhien.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantService restaurantService;

    @Override
    public Category createCategory(String name, Long userId) throws Exception {
        //Look up the restaurant by the OWNER'S ID, not the Restaurant's PK
        Restaurant restaurant = restaurantService.findRestaurantByUserId(userId);
        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurant);
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findCategoryByRestaurantId(Long restaurantId) throws Exception {
        return categoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public Category findCategoryById(Long categoryId) throws Exception {
        Optional<Category> optCategory = categoryRepository.findById(categoryId);
        if (optCategory.isEmpty())
            throw new Exception("Category not found with category id: " + categoryId);
        return optCategory.get();
    }
}
