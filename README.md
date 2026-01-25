# Online Food Ordering System

This project is a comprehensive backend solution for an online food delivery platform. It supports user authentication, restaurant management, food cataloging, and order processing.


## 🚀 Features
### Core Functionality
- Authentication & Authorization: Secure signup and login using JWT (JSON Web Tokens) and Spring Security.
- Multi-Role Support: Distinct functionalities for CUSTOMER and RESTAURANT_OWNER roles.
- Restaurant Management: Admins can create, update, delete, and manage the status (open/closed) of their restaurants.
- Food Catalog: Searchable food items with filters for vegetarian, seasonal, and specific categories.
- Order System: Customers can place orders and view their entire order history.
- Shopping Cart: Automatic cart creation upon user registration.


### 🛠️ Tech Stack
- Framework: Spring Boot 3.4.12
- Security: Spring Security with JWT
- Database: PostgreSQL
- ORM: Spring Data JPA
- Build Tool: Maven
- Utilities: Lombok (for reducing boilerplate code)

### 🔌 API Endpoints (Highlights)

#### Authentication
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/auth/signup` | Register a new user and create a cart. |
| POST | `/auth/signin` | Authenticate and receive a JWT. |

#### User
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/api/users/profile` | Retrieve the profile of the user associated with the provided JWT. |

#### Restaurant
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/api/restaurants/search` | Search for restaurants based on a keyword. |
| GET | `/api/restaurants` | Retrieve a list of all available restaurants. |
| GET | `/api/restaurants/{restaurantId}` | Retrieve detailed information for a specific restaurant by its ID. |
| PUT | `/api/restaurants/{restaurantId}/add-favorites` | Add or remove a restaurant from the user's favorites list. |

#### Admin Restaurant
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/api/admin/restaurants` | Create a new restaurant for the authenticated user. |
| PUT | `/api/admin/restaurants` | Update the details of the restaurant owned by the authenticated user. |
| DELETE | `/api/admin/restaurants` | Delete the restaurant owned by the authenticated user. |
| PUT | `/api/admin/restaurants/status` | Toggle the open/closed status of the user's restaurant. |
| GET | `/api/admin/restaurants/user` | Retrieve the restaurant details linked to the authenticated user's ID. |

#### Admin Food
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/api/admin/food` | Create and add a new food item to the user's restaurant. |
| DELETE | `/api/admin/food/{foodId}` | Remove a specific food item from the restaurant (requires ownership check). |
| PUT | `/api/admin/food/{foodId}` | Update the availability status (stock) of a specific food item. |

#### Food
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/api/food/search` | Search for food items across restaurants by keyword. |
| GET | `/api/food/restaurant/{restaurantId}` | Get food items from a specific restaurant with filters. |

#### Cart
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| PUT | `/api/cart/add` | Add an item to the user's shopping cart. |
| PUT | `/api/cart-item/update` | Update the quantity of a specific item already in the cart. |
| DELETE | `/api/cart-item/{cartItemId}/remove` | Remove a specific item from the cart. |
| PUT | `/api/cart/clear` | Remove all items from the user's cart. |
| GET | `/api/cart` | Retrieve the current state of the authenticated user's cart. |

#### Order
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/api/order` | Place a new food order. |
| GET | `/api/order/user` | Retrieve the order history for the authenticated user. |

#### Admin Order
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/api/admin/order/restaurant` | Retrieve all orders placed at the user's restaurant. |
| PUT | `/api/admin/order/restaurant/{orderId}/{orderStatus}` | Update the status of a specific order. |

#### Category
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/api/admin/category` | Create a new food category for the user's restaurant. |
| GET | `/api/category/restaurant` | Retrieve all food categories associated with the user's restaurant. |

#### Ingredients
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/api/admin/ingredients/category` | Create a new ingredient category. |
| POST | `/api/admin/ingredients/item` | Create a new ingredient item under a specific category. |
| PUT | `/api/admin/ingredients/{itemId}/stock` | Toggle the stock availability of a specific ingredient. |
| GET | `/api/admin/ingredients/restaurant` | List all ingredients belonging to the user's restaurant. |
| GET | `/api/admin/ingredients/restaurant/category` | List all ingredient categories belonging to the user's restaurant. |
