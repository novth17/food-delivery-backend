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

    //UserDetailsService depends on user repo, so we inject an instance of userRepo to this UserDetailsService when application starts up
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username); // Spring data will parse this

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        USER_ROLE role = user.getRole();
        if (role == null) {
            user.setRole(USER_ROLE.ROLE_CUSTOMER);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }


}