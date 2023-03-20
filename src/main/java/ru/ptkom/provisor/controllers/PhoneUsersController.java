package ru.ptkom.provisor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.service.ConfigGeneratorForSNRVP5x;
import ru.ptkom.provisor.service.ConfigGeneratorForSPA9xx;
import ru.ptkom.provisor.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;


@Slf4j
@Controller
public class PhoneUsersController {


    @Autowired
    private PBXUserDAO pbxUserDAO;
    @Autowired
    private RequestService requestService;
    @Autowired
    private ConfigGeneratorForSPA9xx configGeneratorForSPA9xx;
    @Autowired
    private ConfigGeneratorForSNRVP5x configGeneratorForSNRVP5x;


    private static String[] LIST_OF_MODELS_OF_PHONES_IN_COMPANY= {"spa9XX","vp5X","grandstream"};


    @GetMapping("/workers")
    public String showPBXUsers(Map<String, Object> model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        Iterable<PBXUser> users = pbxUserDAO.getAllUsers();
        model.put("users", users);
        return "users/workers/show";
    }


    @GetMapping("/workers/{id}")
    public String showWorkersData(@PathVariable("id") Long id, Model model, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        PBXUser userData = pbxUserDAO.getUserById(id);
        model.addAttribute("user", userData);
        return "users/workers/user";
    }


    @GetMapping("/workers/{id}/edit")
    public String showFormToEditWorkersData(@PathVariable("id") Long id, Model model, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        PBXUser userData = pbxUserDAO.getUserById(id);
        model.addAttribute("user", userData);
        model.addAttribute("list",LIST_OF_MODELS_OF_PHONES_IN_COMPANY);
        return "users/workers/edit";
    }


    @PatchMapping("/workers/edit")
    public String updateWorkersData(@ModelAttribute("user") PBXUser user, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send PATCH request to page: " + request.getRequestURI());
        pbxUserDAO.update(user);
        if (user.getPhoneModel().equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[0])) {
            configGeneratorForSPA9xx.generateConfigFile(user.getNumber(), user.getMac());
        } else if (user.getPhoneModel().equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[1])) {
            configGeneratorForSNRVP5x.generateConfigFile(user.getNumber(), user.getMac());
        }
        return "redirect:/workers";
    }


    @DeleteMapping("/workers/delete")
    public String deleteWorker(@ModelAttribute("user") PBXUser user, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send DELETE request to page: " + request.getRequestURI());
        pbxUserDAO.delete(user.getId());
        return "redirect:/workers";
    }


    @GetMapping("/workers/add")
    public String addPBXUsersForm(Map<String, Object> model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        Iterable<PBXUser> users = pbxUserDAO.getAllUsers();
        model.put("users", users);
        model.put("list",LIST_OF_MODELS_OF_PHONES_IN_COMPANY);
        model.put("user", new PBXUser());
        return "users/workers/add";
    }


    @PostMapping("/workers/add")
    public String addPBXUser(@ModelAttribute PBXUser user, Map<String, Object> model, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send POST request to page: " + request.getRequestURI());
        if (user.getName().equals("") || user.getNumber().equals("") || user.getMac().equals("")) {
            return "redirect:/workers";
        }
        model.put("user", user);
        pbxUserDAO.saveUser(user);
        Iterable<PBXUser> users = pbxUserDAO.getAllUsers();
        model.put("users", users);
        if (user.getPhoneModel().equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[0])) {
            configGeneratorForSPA9xx.generateConfigFile(user.getNumber(), user.getMac());
        } else if (user.getPhoneModel().equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[1])) {
            configGeneratorForSNRVP5x.generateConfigFile(user.getNumber(), user.getMac());
        }
        return "redirect:/workers";
    }
}
