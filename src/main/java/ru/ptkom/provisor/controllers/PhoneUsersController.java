package ru.ptkom.provisor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.exception.PBXUserNotFoundException;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.service.ConfigGeneratorForSNRVP5x;
import ru.ptkom.provisor.service.ConfigGeneratorForSPA9xx;
import ru.ptkom.provisor.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@RestController
public class PhoneUsersController {


    private final PBXUserDAO pbxUserDAO;
    private final RequestService requestService;
    private final ConfigGeneratorForSPA9xx configGeneratorForSPA9xx;
    private final ConfigGeneratorForSNRVP5x configGeneratorForSNRVP5x;

    private static final String[] LIST_OF_MODELS_OF_PHONES_IN_COMPANY= {"spa9XX","vp5X","grandstream"};

    public PhoneUsersController(PBXUserDAO pbxUserDAO, RequestService requestService, ConfigGeneratorForSPA9xx configGeneratorForSPA9xx, ConfigGeneratorForSNRVP5x configGeneratorForSNRVP5x) {
        this.pbxUserDAO = pbxUserDAO;
        this.requestService = requestService;
        this.configGeneratorForSPA9xx = configGeneratorForSPA9xx;
        this.configGeneratorForSNRVP5x = configGeneratorForSNRVP5x;
    }


    @GetMapping(value = "/workers", produces="application/json")
    public ResponseEntity<Iterable<PBXUser>> showPBXUsers(Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        Iterable<PBXUser> users = pbxUserDAO.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @GetMapping(value = "/workers/{id}", produces="application/json")
    public ResponseEntity<PBXUser> showPBXUserData(@PathVariable("id") Long id, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        PBXUser userData = pbxUserDAO.getUserById(id);
        return ResponseEntity.ok(userData);
    }



    @PatchMapping("/workers/edit")
    public ResponseEntity updateWorkersData(@RequestBody PBXUser user, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send PATCH request to page: " + request.getRequestURI());

        try {
            pbxUserDAO.update(user);
            if (user.getPhoneModel().equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[0])) {
                configGeneratorForSPA9xx.generateConfigFile(user.getNumber(), user.getMac());
            } else if (user.getPhoneModel().equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[1])) {
                configGeneratorForSNRVP5x.generateConfigFile(user.getNumber(), user.getMac());
            }
            return ResponseEntity.ok().build();
        } catch (PBXUserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


    @DeleteMapping("/workers/delete/{id}")
    public ResponseEntity deleteWorker(@PathVariable("id") Long id, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send DELETE request to page: " + request.getRequestURI());
        pbxUserDAO.delete(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/workers/add")
    public ResponseEntity addPBXUser(@RequestBody PBXUser user, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send POST request to page: " + request.getRequestURI());
        if (user.getName().equals("") || user.getNumber().equals("") || user.getMac().equals("")) {
            return ResponseEntity.badRequest().build();
        }
        pbxUserDAO.saveUser(user);
        if (user.getPhoneModel().equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[0])) {
            configGeneratorForSPA9xx.generateConfigFile(user.getNumber(), user.getMac());
        } else if (user.getPhoneModel().equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[1])) {
            configGeneratorForSNRVP5x.generateConfigFile(user.getNumber(), user.getMac());
        }
        return ResponseEntity.ok().build();
    }
}
