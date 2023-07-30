package ru.ptkom.provisor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.ptkom.provisor.dao.ECSSUserDataDAO;
import ru.ptkom.provisor.models.alias_list2.AliasType;
import ru.ptkom.provisor.models.sip_user_show.UserType;
import ru.ptkom.provisor.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
public class AliasesController {


    private final ECSSUserDataDAO ecssUserDataDAO;
    private final RequestService requestService;

    public AliasesController(ECSSUserDataDAO ecssUserDataDAO, RequestService requestService) {
        this.ecssUserDataDAO = ecssUserDataDAO;
        this.requestService = requestService;
    }


    @GetMapping(value = "/aliases", produces="application/json")
    public ResponseEntity<List<AliasType>> getAliases(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        List<AliasType> aliases = ecssUserDataDAO.listOfAliases();
        return ResponseEntity.ok(aliases);
    }


    @GetMapping(value = "/aliases/{number}", produces="application/json")
    public ResponseEntity<UserType> showAliasParameters(@PathVariable("number") String number, Model model, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        UserType aliasData = ecssUserDataDAO.allAliasData(number);
        return ResponseEntity.ok(aliasData);
    }
}
