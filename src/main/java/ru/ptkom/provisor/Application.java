package ru.ptkom.provisor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import ru.ptkom.provisor.config.RestTemplateConfig;
import ru.ptkom.provisor.config.SpringConfig;
import ru.ptkom.provisor.config.WebSecurityConfig;

import java.io.IOException;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {


    public static void main(String[] args) throws IOException {
//        SpringApplication
//                .run(Application.class, args);
        new SpringApplicationBuilder()
                .parent(SpringConfig.class)
                .child(WebSecurityConfig.class)
                .sibling(RestTemplateConfig.class)
                .run(args);
    }

//Закоментированный код работает без Томката, но не работает в Томкате. Раскоментированный код - наоборот
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
//        return applicationBuilder
//                .sources(Application.class)
//                .parent(SpringConfig.class)
//                .child(WebSecurityConfig.class)
//                .sibling(RestTemplateConfig.class);
//    }
}