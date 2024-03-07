package ru.tarasov.springcourse.FirstSecurApp.config;

//главный класс для настройки securityconfig


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.tarasov.springcourse.FirstSecurApp.security.CustomAuthenticationProvider;


//анотация дает понять, что этот класс предназначен для конфигурации spring.security
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true )
public class SecurityConfig {



    private CustomAuthenticationProvider authProvider;
    private UserDetailsService userDetailsService;
    private final JWTFilter jwtFilter;


    @Autowired
    public SecurityConfig(CustomAuthenticationProvider authProvider, 
                          UserDetailsService userDetailsService, JWTFilter jwtFilter) {
        this.authProvider = authProvider;
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }


    
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(getPasswordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();

    }
    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
       
                http
                        .csrf(csrf -> csrf.disable())
                        .authorizeRequests()
                        .requestMatchers("/auth/login", "auth/registration", "/error").permitAll()
                        .anyRequest().hasAnyRole("USER", "ADMIN")
                .and()
                .formLogin((formLogin) -> formLogin
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/process_login")
                        .defaultSuccessUrl("/hello", true)
                        .failureUrl("/auth/login?error"))
                        .logout((logout)->logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login"))
                        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

               http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}