package com.food.delivery.filters;

import ch.qos.logback.core.util.StringUtil;
import com.food.delivery.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
     @Autowired
    private JwtUtil jwtUtil;
     @Autowired
     private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // validate the token
        // get the header from request

       final String auth =  request.getHeader("Authorization");
       if(StringUtils.hasText(auth) && auth.startsWith("Bearer ")){
           String token = auth.substring(7);
           String email = jwtUtil.exractUsername(token);
  // security cotntext will be null intially
           if(email!= null && SecurityContextHolder.getContext().getAuthentication() == null){
               UserDetails user = userDetailsService.loadUserByUsername(email);
               if(jwtUtil.isvalidToken(token, user)){
                   UsernamePasswordAuthenticationToken authtok = new UsernamePasswordAuthenticationToken(user , null,user.getAuthorities());
                   authtok.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authtok);


               }

           }

       }
        filterChain.doFilter(request,response);

    }
}
