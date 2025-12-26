package com.lilyhien.service;

import com.lilyhien.model.USER_ROLE;
import com.lilyhien.model.User;
import com.lilyhien.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    //constructor dependency injection
    public CustomerUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // find the user
        User user = userRepository.findByEmail(username);

        //check if user role is null
        if (user == null)
            throw new UsernameNotFoundException("User not found with email: " + username);

        //handle role safely
        USER_ROLE role = user.getRole(); //no need to check null coz we assigned in User class

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}