package ru.ptkom.provisor.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;


@Controller
public class IndexController {


    @GetMapping("/")
    public String defaultPage(Model model, Principal principal) {
        String name = principal.getName();
        model.addAttribute("name", name);
        return "navigation/menu";
    }


    @GetMapping("/basic")
    public String basicToMenuRedirect(){
        return "redirect:/";
    }


}