package com.lilyhien.repository;

import com.lilyhien.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


//https://www.geeksforgeeks.org/springboot/spring-boot-jparepository-with-example/
// JpaRepository is an interface in Spring Data JPA.
// It is built on top of the JPA (Java Persistence API) and provides all the basic methods you need for handling data.
//T: The type of the entity (e.g., User, Product)
//ID: The type of the primary key (e.g., Long, Integer)
public interface UserRepository  extends JpaRepository<User,Long> {

    //find...By: Tells Spring this is a "SELECT" query. SELECT * FROM users WHERE email = 'test@example.com';
    //Email: Tells Spring to look for a field (property) named email inside the User class.
    User findByEmail(String email);
}
