package com.lilyhien.service;

import com.lilyhien.model.IngredientsCategory;
import com.lilyhien.model.IngredientsItem;
import com.lilyhien.model.Restaurant;
import com.lilyhien.repository.IngredientsCategoryRepository;
import com.lilyhien.repository.IngredientsItemRepository;
import com.lilyhien.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class IngredientsServiceImpl implements  IngredientsService {

    private final IngredientsItemRepository ingredientsItemRepository;
    private final IngredientsCategoryRepository ingredientsCategoryRepository;
    private final RestaurantService restaurantService;
    private final UserRepository userRepository;

    @Override
    public IngredientsCategory createIngredientCategory(String name, Long userId) throws Exception {

        Restaurant restaurant = restaurantService.findRestaurantByUserId(userId);

        IngredientsCategory ingredientsCategory = new IngredientsCategory();
        ingredientsCategory.setName(name);
        ingredientsCategory.setRestaurant(restaurant);

        return ingredientsCategoryRepository.save(ingredientsCategory);
    }

    @Override
    public IngredientsCategory findIngredientCategoryById(Long id) throws Exception {
        Optional<IngredientsCategory> optIngredientsCategory = ingredientsCategoryRepository.findById(id);
        if (optIngredientsCategory.isEmpty()) {
            throw new Exception("Category not found with category id: " + id);
        }
        return optIngredientsCategory.get();
    }

    @Override
    public List<IngredientsCategory> findIngredientCategoryByRestaurantUserId(Long userId) throws Exception {
        // service handles the business logic, Which restaurant does this USER own?
        Restaurant restaurant = restaurantService.findRestaurantByUserId(userId);

        // it uses that specific restaurant's ID for the database query
        return ingredientsCategoryRepository.findByRestaurantId(restaurant.getId());
    }

    //ingredientCategoryId should not be completely trusted that correct
    //check if the person holding the key actually owns the door they are trying to unlock
    @Override
    public IngredientsItem createIngredientsItem(Long userId, String ingredientName, Long ingredientCategoryId) throws Exception {

        Restaurant restaurant = restaurantService.findRestaurantByUserId(userId);
        IngredientsCategory ingredientsCategory = findIngredientCategoryById(ingredientCategoryId);

        IngredientsItem item = new IngredientsItem();
        item.setName(ingredientName);
        item.setRestaurant(restaurant);
        item.setCategory(ingredientsCategory);

        if (!ingredientsCategory.getRestaurant().getId().equals(restaurant.getId())) {
            throw new Exception("You cannot add ingredients to another restaurant's category");
        }
        IngredientsItem ingredientsItem = ingredientsItemRepository.save(item);
        //after save, need to add to category List java object too, so they can sync with database
        ingredientsCategory.getIngredients().add(item);

        return ingredientsItem;
    }

    @Override
    public List<IngredientsItem> findRestaurantsIngredients(Long userId) throws Exception {

        Restaurant restaurant = restaurantService.findRestaurantByUserId(userId);
        return ingredientsItemRepository.findByRestaurantId(restaurant.getId());
    }

    @Override
    public IngredientsItem updateStock(Long ingredientId, Long userId) throws Exception {

        Optional<IngredientsItem> optItem = ingredientsItemRepository.findById(ingredientId);
        if (optItem.isEmpty()) {
            throw new Exception("Ingredient not found with item id: " + ingredientId);
        }

        IngredientsItem item = optItem.get();

        //verification
        Restaurant restaurant = restaurantService.findRestaurantByUserId(userId);
        if (!item.getRestaurant().getId().equals(restaurant.getId())) {
            throw new Exception("Unauthorized: This ingredient belongs to another restaurant.");
        }

        item.setIsInStock(!item.getIsInStock());
        return ingredientsItemRepository.save(item);
    }
}