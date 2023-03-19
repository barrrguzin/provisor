package ru.ptkom.provisor.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.ptkom.provisor.service.UserService;

import java.util.Properties;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Autowired
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeHttpRequests()
                .antMatchers("/static/css/other.css").permitAll()
                .antMatchers("/static/css/sing.css").permitAll()
                .antMatchers("/static/img/icon.ico").permitAll()
                .antMatchers("/static/**").authenticated()
                .antMatchers("/").authenticated()
                .antMatchers("/aliases/**").hasAnyRole("ADMIN", "SUPERADMIN")
                .antMatchers("/workers/**").hasAnyRole("ADMIN", "SUPERADMIN")
                .antMatchers("/config/make").hasAnyRole("ADMIN", "SUPERADMIN")
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutSuccessUrl("/login").deleteCookies("JSESSIONID")
                .and()
                .rememberMe().key("uniqueAndSecret");

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        inMemoryConfigurer()
                .withUser("barguzin")
                .password("{noop}1488228")
                .authorities("SUPERADMIN")
                .roles("SUPERADMIN")
                .and()
                .configure(authenticationManagerBuilder);

        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }


    private InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>
    inMemoryConfigurer() {
        return new InMemoryUserDetailsManagerConfigurer<>();
    }
}