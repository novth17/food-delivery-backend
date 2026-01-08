package com.lilyhien.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/*
 * In Spring, a Bean is an object whose lifecycle is managed by the IoC (Inversion of Control) container.
 * https://docs.spring.io/spring-framework/reference/core/beans/introduction.html
 * Declaring this method with @Bean explicitly provides a custom SecurityFilterChain
 * implementation to the Spring context.
 * This overrides Spring Security’s default configuration and defines how authentication,
 * authorization, login, logout, and request filtering are handled.+
 *
 * Spring Security automatically detects a SecurityFilterChain bean and uses it
 * as the core configuration for HTTP security.
 */

@Configuration
@EnableWebSecurity
public class AppConfig {

    //When you define your security configuration bean, you are actually creating an implementation of this interface using a builder.
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        //server doesn't "remember" the user, every single request must prove who the user is.
        // This is why this setting is almost always used in REST APIs that use JWT (JSON Web Tokens).
        //neither server stores a session, it doesn't matter which one handles the request.
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(Authorize -> Authorize
                        .requestMatchers("/api/admin/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN") //only these role can access this api
                        .requestMatchers("/api/**").authenticated() // need to provide JWT token to access this api as it stateless
                        .anyRequest().permitAll() // all can access no need authentication
                ).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    //Browser, are you allowed to make THIS request?
    //Cross-Origin Resource Sharing policy is a browser rule that decides whether a web page is allowed to read responses from another origin.
    //define the rules that tell browser whether it's allowed to call my backend, otherwise it blocks everything
    private CorsConfiguration corsConfiguration()  {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedOrigins(Arrays.asList(
                "delivery-haus.vercel.app",
                "http://localhost:3000"
        ));

        //include CRUD
        corsConfiguration.setAllowedMethods(Collections.singletonList("*")); //FE can use all crud methods
        corsConfiguration.setAllowCredentials(true); //Browser may send credentials with the request (Cookies Authorization headers TLS client certs)
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));//allow FE send any request header for auth(jwt), content-type(json), custom header
        corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));//expose header in browser for FE to read
        corsConfiguration.setMaxAge(3600L); // cache cors permission for 1h
        return corsConfiguration;
    }

    //is an interface that has only 1 abstract method, we override it
    //create an anonymous class that implements the interface
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request ->  corsConfiguration(); //lamda match interface CorsConfigurationSource
    }

    //bcrypt password first, store in db 4 security
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

