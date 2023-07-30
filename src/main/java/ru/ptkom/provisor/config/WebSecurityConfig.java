package ru.ptkom.provisor.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.ptkom.provisor.service.ApplicationPropertiesFileService;
import ru.ptkom.provisor.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final ApplicationPropertiesFileService applicationPropertiesFileService;

    private static String viewApplicationURL;

    public WebSecurityConfig(UserService userService, ApplicationPropertiesFileService applicationPropertiesFileService) {
        this.userService = userService;
        this.applicationPropertiesFileService = applicationPropertiesFileService;
        viewApplicationURL = applicationPropertiesFileService.getViewApplicationURL();
    }

//    @Autowired
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                .and()
//                .cors().disable()
//                .authorizeHttpRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/static/css/other.css").permitAll()
//                .antMatchers("/static/css/sing.css").permitAll()
//                .antMatchers("/static/img/icon.ico").permitAll()
//                .antMatchers("/static/img/error.jpg").permitAll()
//                .antMatchers("/static/**").authenticated()
//                //.antMatchers("/").authenticated()
//                .antMatchers("/aliases/**").hasAnyRole("ADMIN", "SUPERADMIN")
//                .antMatchers("/workers/**").hasAnyRole("ADMIN", "SUPERADMIN")
//                .antMatchers("/config/make").hasAnyRole("ADMIN", "SUPERADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage("/login").permitAll()
//                .and()
//                .logout().deleteCookies("JSESSIONID", "XSRF-TOKEN").logoutUrl("/logout").invalidateHttpSession(true)
//                .and()
//                .rememberMe().key("uniqueAndSecret")
//                .and()
//                .httpBasic();

        http.cors();
        http.csrf().disable();
        http.httpBasic().disable();

        http
                .authorizeRequests()
                //.antMatchers(HttpMethod.GET,"/login").denyAll()
                .antMatchers(HttpMethod.GET,"/snr-vp/**").permitAll()
                .antMatchers(HttpMethod.GET,"/linksys/**").permitAll()
                .antMatchers("/**")
                .fullyAuthenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                            //loginProcessingUrl("/login")
                .successHandler(appAuthenticationSuccessHandler())
                .failureHandler(appAuthenticationFailureHandler());

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://" + viewApplicationURL, "http://" + viewApplicationURL, "https://localhost:4200", "http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
        strictHttpFirewall.setAllowUrlEncodedDoubleSlash(true);
        return strictHttpFirewall;
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


    @Bean
    public AuthenticationSuccessHandler appAuthenticationSuccessHandler(){
        return new AppAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler appAuthenticationFailureHandler(){
        return new AppAuthenticationFailureHandler();
    }


    private InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>
    inMemoryConfigurer() {
        return new InMemoryUserDetailsManagerConfigurer<>();
    }



    public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
        protected void handle(HttpServletRequest request, HttpServletResponse response,
                              Authentication authentication) throws IOException, ServletException {
        }

    }

    public class AppAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
        protected void handle(HttpServletRequest request, HttpServletResponse response,
                              Authentication authentication) throws IOException, ServletException {
        }

    }
}