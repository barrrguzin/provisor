package ru.ptkom.provisor.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.service.RequestService;


import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@Controller
public class IndexController {


    @Autowired
    private RequestService requestService;


    @GetMapping("/")
    public String defaultPage(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String name = principal.getName();
        model.addAttribute("name", name);
        return "navigation/menu";
    }


    @GetMapping("/basic")
    public String basicToMenuRedirect(Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        return "redirect:/";
    }


    @GetMapping("/about")
    public String getInformation(Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        return "about";
    }
}