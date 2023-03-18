package ru.ptkom.provisor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ptkom.provisor.client.ECSSAPIClient;
import ru.ptkom.provisor.dao.ECSSUserDataDAO;
import ru.ptkom.provisor.models.sipUsers.OutUsers;

import java.util.List;

@Controller
public class AliasesController {


    @Autowired
    private ECSSUserDataDAO ecssUserDataDAO;
    @Autowired
    private ECSSAPIClient ecssApiClient;


    @GetMapping("/aliases")
    public String getAliases(Model model) {
        List<List<String>> aliases = ecssUserDataDAO.listOfAliasesNames();
        model.addAttribute("aliases", aliases);
        return "users/aliases/show";
    }


    @GetMapping("/aliases/{number}")
    public String showAliasParameters(@PathVariable("number") String number, Model model){
        //Паараметры конкретного пользователя из API
        OutUsers.User aliasData = ecssUserDataDAO.allAliasData(number);
        model.addAttribute("data", aliasData);
        return "users/aliases/alias";
    }
}
