package com.lilyhien.service;

import com.lilyhien.dto.FavoriteRestaurantDto;
import com.lilyhien.model.Address;
import com.lilyhien.model.Restaurant;
import com.lilyhien.model.User;
import com.lilyhien.repository.AddressRepository;
import com.lilyhien.repository.RestaurantRepository;
import com.lilyhien.repository.UserRepository;
import com.lilyhien.requestDto.CreateRestaurantRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class RestaurantServiceImpl  implements  RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public Restaurant createRestaurant(CreateRestaurantRequest request, User user) {

        Address address = addressRepository.save(request.getAddress());

        Restaurant restaurant = new Restaurant();
        restaurant.setAddress(address);
        restaurant.setContactInformation(request.getContactInformation());
        restaurant.setCuisineType(request.getCuisineType());
        restaurant.setDescription(request.getDescription());
        restaurant.setImages(request.getImages());
        restaurant.setName(request.getName());
        restaurant.setOpeningHours(request.getOpeningHours());
        restaurant.setRegistrationDate(LocalDateTime.now());
        restaurant.setOwner(user);

        //https://www.baeldung.com/spring-data-crud-repository-save
        // Once the restaurant object is fully built, we save it to repo, create a new entry in the database table
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant updateRestaurant(Long restaurantId, CreateRestaurantRequest updatedRestaurant) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);

        if (updatedRestaurant.getCuisineType() != null)
            restaurant.setCuisineType(updatedRestaurant.getCuisineType());
        if (updatedRestaurant.getDescription() != null)
            restaurant.setDescription(updatedRestaurant.getDescription());
        if (updatedRestaurant.getName() != null)
            restaurant.setName(updatedRestaurant.getName());
        if (updatedRestaurant.getContactInformation() != null)
            restaurant.setContactInformation(updatedRestaurant.getContactInformation());
        if (updatedRestaurant.getOpeningHours() != null)
            restaurant.setOpeningHours(updatedRestaurant.getOpeningHours());
        if (updatedRestaurant.getImages() != null)
            restaurant.setImages(updatedRestaurant.getImages());

        //only update the address when the incoming address is not null!
        if (updatedRestaurant.getAddress() != null) {
            Address existing = restaurant.getAddress();
            Address incoming = updatedRestaurant.getAddress();

            if (incoming.getStreetAddress() != null)
                existing.setStreetAddress(incoming.getStreetAddress());
            if (incoming.getCity() != null)
                existing.setCity(incoming.getCity());
            if (incoming.getStateProvince() != null)
                existing.setStateProvince(incoming.getStateProvince());
            if (incoming.getPostalCode() != null)
                existing.setPostalCode(incoming.getPostalCode());
            if (incoming.getCountry() != null)
                existing.setCountry(incoming.getCountry());
        }

        return restaurantRepository.save(restaurant);
    }

    @Override
    public void deleteRestaurant(Long restaurantId) throws Exception {
        Restaurant restaurant = findRestaurantById(restaurantId);
        // Since User -> Restaurant is unidirectional in User entity,
        // deleting the Restaurant row is usually enough.
        // But if ever add a 'restaurant' field to User, MUST null it here first.
        // Use the repository to remove it from the database
        // This will trigger a SQL DELETE statement via Hibernate
        restaurantRepository.delete(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurant() {
        return restaurantRepository.findAll();
    }

    @Override
    public List<Restaurant> searchRestaurant(String keyword) {
        return restaurantRepository.findBySearchQuery(keyword);
    }

    @Override
    public Restaurant findRestaurantById(Long restaurantId) throws Exception {
        //opt is a box can contain 0 or 1 restaurant, if the "box" is empty, throw. if not, get the restaurant from the box
        Optional<Restaurant> optRestaurant = restaurantRepository.findById(restaurantId);
        if (optRestaurant.isEmpty()) {
            throw new Exception("Restaurant not found with restaurant id: " + restaurantId);
        }
        return optRestaurant.get();
    }

    @Override
    public Restaurant findRestaurantByUserId(Long userId) throws Exception {
        Restaurant restaurant = restaurantRepository.findByOwnerId(userId);
        if (restaurant == null) {
            throw new Exception("Restaurant not found with owner id: " + userId);
        }
        return restaurant;
    }

    @Override
    public FavoriteRestaurantDto addToFavorites(Long restaurantId, User user) throws Exception {

        Restaurant restaurant = findRestaurantById(restaurantId);
        FavoriteRestaurantDto favDto = new FavoriteRestaurantDto();
        favDto.setDescription(restaurant.getDescription());
        favDto.setThumbnailImage(favDto.getThumbnailImage());
        favDto.setTitle(restaurant.getName());
        favDto.setId(restaurantId);

        boolean wasRemoved = user.getFavorites().removeIf(fav -> fav.getId().equals(restaurantId));
        if (!wasRemoved) {
            user.getFavorites().add(favDto);
        }
        userRepository.save(user);
        return favDto;
    }

    public boolean shouldRemove(FavoriteRestaurantDto fav, Long restaurantId) {
        return fav.getId().equals(restaurantId);
    }

    @Override
    public Restaurant updateRestaurantStatus(Long id) throws Exception {
        Restaurant restaurant = findRestaurantById(id);
        restaurant.setOpen(!restaurant.isOpen());
        return restaurantRepository.save(restaurant);
    }
}
