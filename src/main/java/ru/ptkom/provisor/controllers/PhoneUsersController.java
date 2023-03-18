package ru.ptkom.provisor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.models.PBXUser;

import java.util.Map;

@Controller
public class PhoneUsersController {


    @Autowired
    private PBXUserDAO pbxUserDAO;


    private static String[] LIST_OF_MODELS_OF_PHONES_IN_COMPANY= {"spa9XX","vp5X","grandstream"};


    @GetMapping("/workers")
    public String showPBXUsers(Map<String, Object> model) {
        Iterable<PBXUser> users = pbxUserDAO.getAllUsers();
        model.put("users", users);
        return "users/workers/show";
    }


    @GetMapping("/workers/{id}")
    public String showWorkersData(@PathVariable("id") Long id, Model model){
        PBXUser userData = pbxUserDAO.getUserById(id);
        model.addAttribute("user", userData);
        return "users/workers/user";
    }


    @GetMapping("/workers/{id}/edit")
    public String showFormToEditWorkersData(@PathVariable("id") Long id, Model model){
        PBXUser userData = pbxUserDAO.getUserById(id);
        model.addAttribute("user", userData);
        model.addAttribute("list",LIST_OF_MODELS_OF_PHONES_IN_COMPANY);
        return "users/workers/edit";
    }


    @PatchMapping("/workers/edit")
    public String updateWorkersData(@ModelAttribute("user") PBXUser user){
        pbxUserDAO.update(user);
        return "redirect:/workers";
    }


    @DeleteMapping("/workers/delete")
    public String deleteWorker(@ModelAttribute("user") PBXUser user){

        pbxUserDAO.delete(user.getId());
        return "redirect:/workers";
    }


    @GetMapping("/workers/add")
    public String addPBXUsersForm(Map<String, Object> model) {
        Iterable<PBXUser> users = pbxUserDAO.getAllUsers();
        model.put("users", users);
        model.put("list",LIST_OF_MODELS_OF_PHONES_IN_COMPANY);
        model.put("user", new PBXUser());
        return "users/workers/add";
    }


    @PostMapping("/workers/add")
    public String addPBXUser(@ModelAttribute PBXUser user, Map<String, Object> model){
        if (user.getName().equals("") || user.getNumber().equals("") || user.getMac().equals("")) {
            return "redirect:/workers";
        }
        model.put("user", user);
        pbxUserDAO.saveUser(user);
        Iterable<PBXUser> users = pbxUserDAO.getAllUsers();
        model.put("users", users);
        return "redirect:/workers";
    }
}
