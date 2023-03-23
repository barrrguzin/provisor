package ru.ptkom.provisor.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.service.ConfigFileService;
import ru.ptkom.provisor.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@Controller
public class ConfigurationFilesController {


    private static final String[] RESPONSE_LIST = {"Number field is empty!","User with this number not found!"};


    @Autowired
    private PBXUserDAO pbxUserDAO;
    @Autowired
    private ConfigFileService configFileService;
    @Autowired
    private RequestService requestService;


    @GetMapping("/config/make")
    public String takeNumberToMakeConfig(Model model, Principal principal, HttpServletRequest request) {


        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request)
                + "; Try to get page: " + request.getRequestURI());


        model.addAttribute("number", "");
        return "phone/make";
    }


    @PostMapping("/config/make")
    public @ResponseBody String makePhonesConfig(@RequestParam String number, Principal principal, HttpServletRequest request) {


        log.info("User: " + principal.getName()
                + "; From: " + requestService.getClientIp(request)
                + "; Try to send POST request: " + request.getRequestURI());


        if(number.equals("")){
            return RESPONSE_LIST[0];
        }


        String mac = pbxUserDAO.getMacByNumber(number);
        String phoneModel = pbxUserDAO.getPhoneModelByNumber(number);


        if(mac == null || phoneModel == null){
            return RESPONSE_LIST[1];
        }


        return configFileService.defineAndGenerateConfigFile(number, mac, phoneModel);


    }


}