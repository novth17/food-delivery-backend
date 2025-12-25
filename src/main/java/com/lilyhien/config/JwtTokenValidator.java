package com.lilyhien.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.Security;
import java.util.List;

/*
*   JwtTokenValidator does 5 things:
    Read JWT from Authorization header
    Remove "Bearer "
    Verify signature using secret key
    Extract claims (email, authorities)
    Put authenticated user into security context
    *
    * Request → Filter → SecurityContextHolder → Controller
* */

//read more about jwt here
//https://www.jwt.io/introduction#what-is-json-web-token-structure
public class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        //extract [Bearer] token
        if (jwt != null) {
            jwt = jwt.substring(7);

            try {
                //takes secret string and ensures it is turned into a properly formatted SecretKey object

                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes()); //from jjwt library

                //mathematically verify that the token hasn't been tampered with.
                Claims claims= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities")); //get all role

                //ROLE_CUSTOMER, ROLE_ADMIN, ROLE_RESTAUTANT_OWNER
                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auth);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid token!");
            }
        }
        filterChain.doFilter(request, response);
    }
}
