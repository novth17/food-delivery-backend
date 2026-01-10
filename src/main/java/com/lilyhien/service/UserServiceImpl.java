package com.lilyhien.service;

import com.lilyhien.config.JwtProvider;
import com.lilyhien.exception.ResourceNotFoundException;
import com.lilyhien.model.User;
import com.lilyhien.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
//@Transactional no need yet because here only perform read
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    //inject userRepo & jwt provider to this UserServiceImpl constructor
    public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromJwtToken(jwt);
        //null string check is needed because of String.valueOf() can return null
        if (email == null || email.equals("null")) {
            throw new ResourceNotFoundException("Email is empty or User not found with email: " + email);
        }
        return findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }
}
