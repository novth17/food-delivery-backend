package com.lilyhien.controller;

import com.lilyhien.model.IngredientsCategory;
import com.lilyhien.model.IngredientsItem;
import com.lilyhien.model.User;
import com.lilyhien.requestDto.CreateIngredientCategoryRequest;
import com.lilyhien.requestDto.CreateIngredientRequest;
import com.lilyhien.service.IngredientsService;
import com.lilyhien.service.RestaurantService;
import com.lilyhien.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/ingredients")
public class IngredientController {

    private final IngredientsService ingredientsService;
    private final RestaurantService restaurantService;
    private final UserService userService;

    @PostMapping("/category")
    public ResponseEntity<IngredientsCategory> createIngredientsCategory(
            @RequestBody CreateIngredientCategoryRequest request,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        IngredientsCategory ingCategory = ingredientsService.createIngredientCategory(request.getName(), user.getId());
        return new ResponseEntity<>(ingCategory, HttpStatus.CREATED);
    }

    @PostMapping("/item")
    public ResponseEntity<IngredientsItem> createIngredientsItem(
            @RequestBody CreateIngredientRequest request,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        IngredientsItem ingItem = ingredientsService.createIngredientsItem(user.getId(), request.getName(), request.getCategoryId());
        return new ResponseEntity<>(ingItem, HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}/stock")
    public ResponseEntity<IngredientsItem> updateItemStock(
            @PathVariable Long itemId,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        IngredientsItem item = ingredientsService.updateStock(itemId, user.getId());
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

}
