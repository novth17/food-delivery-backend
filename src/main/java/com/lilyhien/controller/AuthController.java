package com.lilyhien.controller;

import com.lilyhien.config.JwtProvider;
import com.lilyhien.model.Cart;
import com.lilyhien.model.User;
import com.lilyhien.repository.CartRepository;
import com.lilyhien.repository.UserRepository;
import com.lilyhien.response.AuthResponse;
import com.lilyhien.service.CustomerUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor //Lombok generates the constructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomerUserDetailService customerUserDetailService;
    private final CartRepository cartRepository;

    //ResponseEntity: shipping box (success 200 or error 400)
    //AuthResponse: the product inside the box - actual data - jwt token & message
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUser(@RequestBody User user) throws Exception {

        if (emailExists(user.getEmail())) {
            throw new Exception("Email is already in used with another account.");
        }
        User createUser = new User();
        createUser.setEmail(user.getEmail());
        createUser.setFullName(user.getFullName());
        createUser.setRole(user.getRole());
        createUser.setPassword(passwordEncoder.encode(user.getPassword())); // form of Bcrypt

        User savedUser = userRepository.save(createUser);

        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register success");
        authResponse.setRole(savedUser.getRole());
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED); //HttpStatusCode is interface, this is impl
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }
}