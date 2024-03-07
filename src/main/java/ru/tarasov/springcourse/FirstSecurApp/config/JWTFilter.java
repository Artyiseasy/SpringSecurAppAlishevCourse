package ru.tarasov.springcourse.FirstSecurApp.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.tarasov.springcourse.FirstSecurApp.Dto.PersonDTO;
import ru.tarasov.springcourse.FirstSecurApp.security.JWTUtil;
import ru.tarasov.springcourse.FirstSecurApp.services.PersonDetailsService;

import java.io.IOException;

// задача: отлавливать все запросы к спринг приложению
// и во всех запросах смотреть на  заголовок в котором будем отпрпалять JWT token который мы получили
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final PersonDetailsService personDetailsService;

    public JWTFilter(JWTUtil jwtUtil, PersonDetailsService personDetailsService) {
        this.jwtUtil = jwtUtil;
        this.personDetailsService = personDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

      String authHeader =  request.getHeader("Authorization");

      if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
          String jwt = authHeader.substring(7);

          if (jwt.isBlank()) {
              response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                      " Invalid JWT token in Bearer Header"); //400
          } else {
              try {
                  String username = jwtUtil.validateTokenAndRetrieveClaim(jwt);
                  UserDetails userDetails = personDetailsService.loadUserByUsername(username);

                  //авторизация пользователя
                  UsernamePasswordAuthenticationToken authToken =
                          new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword()
                                  , userDetails.getAuthorities());

                  if (SecurityContextHolder.getContext().getAuthentication() == null) {
                      SecurityContextHolder.getContext().setAuthentication(authToken);
                  }
              } catch (JWTVerificationException exc) {
                  response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
              }
          }
      }
      //продвигаем фильтр дальше
        filterChain.doFilter(request, response);
    }
}
