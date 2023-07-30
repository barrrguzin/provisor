package ru.ptkom.provisor.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.RoleDAO;
import ru.ptkom.provisor.service.RequestService;


import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@RestController
public class IndexController {


    private final RequestService requestService;
    private final RoleDAO roleDAO;

    public IndexController(RequestService requestService, RoleDAO roleDAO) {
        this.requestService = requestService;
        this.roleDAO = roleDAO;
    }

    private static final String[] LIST_OF_MODELS_OF_PHONES_IN_COMPANY= {"spa9XX","vp5X","grandstream"};
    private static final String[] LIST_OF_SUPPORTED_ROLES = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_USER"};

    @GetMapping(value = "/roles", produces="application/json")
    public ResponseEntity getRolesList(Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        return ResponseEntity.ok(roleDAO.getListOfRoles());
    }


    @GetMapping(value = "/supported_phones", produces="application/json")
    public ResponseEntity<String[]> getSupportedPhonesList(Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        return ResponseEntity.ok(LIST_OF_MODELS_OF_PHONES_IN_COMPANY);
    }
}