package ru.ptkom.provisor.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.ptkom.provisor.service.UserService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Autowired
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService){
        this.userService=userService;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().authorizeHttpRequests()
                .antMatchers("/static/css/other.css").permitAll()
                .antMatchers("/static/css/sing.css").permitAll()
                .antMatchers("/static/img/icon.ico").permitAll()
                .antMatchers("/static/**").authenticated()
                .antMatchers("/").authenticated()
                .antMatchers("/aliases/**").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/workers/**").hasAnyRole("ADMIN","SUPERADMIN")
                .antMatchers("/config/make").hasAnyRole("ADMIN","SUPERADMIN")
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutSuccessUrl("/login").deleteCookies("JSESSIONID")
                .and()
                .rememberMe().key("uniqueAndSecret");

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }


}