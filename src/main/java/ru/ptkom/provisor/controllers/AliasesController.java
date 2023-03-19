package ru.ptkom.provisor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ptkom.provisor.dao.ECSSUserDataDAO;
import ru.ptkom.provisor.models.sipUsers.OutUsers;
import ru.ptkom.provisor.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
public class AliasesController {


    @Autowired
    private ECSSUserDataDAO ecssUserDataDAO;
    @Autowired
    private RequestService requestService;


    @GetMapping("/aliases")
    public String getAliases(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        List<List<String>> aliases = ecssUserDataDAO.listOfAliasesNames();
        model.addAttribute("aliases", aliases);
        return "users/aliases/show";
    }


    @GetMapping("/aliases/{number}")
    public String showAliasParameters(@PathVariable("number") String number, Model model, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        OutUsers.User aliasData = ecssUserDataDAO.allAliasData(number);
        model.addAttribute("data", aliasData);
        return "users/aliases/alias";
    }
}
