package ru.ptkom.provisor.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ptkom.provisor.service.ConfigGeneratorForSPA9xx;

@RestController
public class ConfigController {


    @Autowired
    private ConfigGeneratorForSPA9xx configGeneratorForSPA9xx;


    @GetMapping("/test")
    public String getConfigForSPA9xx(){

        return configGeneratorForSPA9xx.configGenerator("LOGIN", "PASSWORD", "USERNAME", "DPNM");
    }



}
