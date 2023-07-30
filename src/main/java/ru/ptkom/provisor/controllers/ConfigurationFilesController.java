package ru.ptkom.provisor.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.exception.PBXUserNotFoundException;
import ru.ptkom.provisor.service.ConfigFileService;
import ru.ptkom.provisor.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@RestController
public class ConfigurationFilesController {


    private static final String[] RESPONSE_LIST = {"Number field is empty!","User with this number not found!"};


    private final PBXUserDAO pbxUserDAO;
    private final ConfigFileService configFileService;
    private final RequestService requestService;


    public ConfigurationFilesController(PBXUserDAO pbxUserDAO, ConfigFileService configFileService, RequestService requestService) {
        this.pbxUserDAO = pbxUserDAO;
        this.configFileService = configFileService;
        this.requestService = requestService;
    }


    @PostMapping("/config/make")
    public ResponseEntity makePhonesConfig(@RequestBody String number, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName()
                + "; From: " + requestService.getClientIp(request)
                + "; Try to send POST request: " + request.getRequestURI());
        if(number.equals("")){
            return ResponseEntity.badRequest().build();
        }
        try {
            String mac = pbxUserDAO.getMacByNumber(number);
            String phoneModel = pbxUserDAO.getPhoneModelByNumber(number);
            if(mac == null || phoneModel == null){
                return ResponseEntity.badRequest().build();
            }
            configFileService.defineAndGenerateConfigFile(number, mac, phoneModel);
            return ResponseEntity.ok().build();
        } catch (PBXUserNotFoundException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}