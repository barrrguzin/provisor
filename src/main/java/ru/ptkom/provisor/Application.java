package ru.ptkom.provisor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import ru.ptkom.provisor.service.UdpSocketService;

import java.io.IOException;


@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Autowired
    private UdpSocketService udpSocketService;


    public static void main(String[] args) throws IOException {

        SpringApplication.run(Application.class, args);



    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(Application.class);
    }


//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        udpSocketService.startUdpSocket(5060);
//    }
}