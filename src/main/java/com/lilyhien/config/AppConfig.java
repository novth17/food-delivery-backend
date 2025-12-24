package com.lilyhien.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

//Every request is:
//authenticated
//authorized

/*
 * In Spring, a Bean is an object whose lifecycle is managed by the IoC (Inversion of Control) container.
 * https://docs.spring.io/spring-framework/reference/core/beans/introduction.html
 * Declaring this method with @Bean explicitly provides a custom SecurityFilterChain
 * implementation to the Spring context.
 * This overrides Spring Security’s default configuration and defines how authentication,
 * authorization, login, logout, and request filtering are handled.
 *
 * Spring Security automatically detects a SecurityFilterChain bean and uses it
 * as the core configuration for HTTP security.
 */

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement()

        return null;
    }
}

