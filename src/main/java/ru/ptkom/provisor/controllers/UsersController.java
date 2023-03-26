package ru.ptkom.provisor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.UserDAO;
import ru.ptkom.provisor.exception.PBXUserNotFoundException;
import ru.ptkom.provisor.exception.UserNotFoundException;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.service.RequestService;
import ru.ptkom.provisor.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;


@Slf4j
@Controller
public class UsersController {


    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private RequestService requestService;


    private static final String[] LIST_OF_SUPPORTED_ROLES = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_USER"};


    @GetMapping("/users")
    public String usersShow(Map<String, Object> model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        Iterable<User> users = userDAO.getAllUsers();
        model.put("users", users);
        return "users/users/show";
    }


    @GetMapping("/users/{id}")
    public String showUserParameters(@PathVariable("id") Long id, Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());

        try {
            User user = userDAO.getUserById(id);
            model.addAttribute("user", user);
            return "users/users/user";
        } catch (UserNotFoundException e) {
            String[] errorData = {"404", "User with ID " + id + " not found."};
            model.addAttribute("error", errorData);
            return "/error";
        }
    }


    @GetMapping("/users/{id}/edit")
    public String editUserParameters(@PathVariable("id") Long id, Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());

        try {
            User user = userDAO.getUserById(id);
            user.setPassword("");
            model.addAttribute("user", user);
            model.addAttribute("list", LIST_OF_SUPPORTED_ROLES);
            return "users/users/edit";
        } catch (UserNotFoundException e) {
            String[] errorData = {"404", "User with ID " + id + " not found."};
            model.addAttribute("error", errorData);
            return "/error";
        }
    }


    @PatchMapping("/users/edit")
    public String updateUsersData(@ModelAttribute User user, @RequestParam(name = "rolesList", defaultValue = "empty") String[] roles, Model model, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send PATCH request to page: " + request.getRequestURI());
        if (roles[0].equals("empty")){
            model.addAttribute("user", user);
            model.addAttribute("list", LIST_OF_SUPPORTED_ROLES);
            model.addAttribute("result", "Выберете хотябы одну роль!");
            return "users/users/edit";
        }
        String number = user.getPbxUser().getNumber();

        try {
            if (number.equals("")){
                userService.updateUser(user, roles);
            } else {
                userService.updateUser(user,number, roles);
            }
        } catch (PBXUserNotFoundException e) {
            log.warn("User data not updated: " + e.getMessage());
        } catch (UserNotFoundException e) {
            log.warn("User data not updated: " + e.getMessage());
        }

        return "redirect:/users";
    }


    @DeleteMapping("/users/delete")
    public String deleteUsersData(@ModelAttribute("user") User user, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send DELETE request to page: " + request.getRequestURI());
        if (user.getUsername() != null) {
            userDAO.deleteUser(user);
            return "redirect:/users";
        }
        return "redirect:/users";
    }


    @GetMapping("/users/add")
    public String addUsersForm(Map<String, Object> model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        model.put("user", new User());
        model.put("list", LIST_OF_SUPPORTED_ROLES);
        return "users/users/add";
    }


    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user, @RequestParam("rolesList") String[] roles, Principal principal, HttpServletRequest request){
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send POST request to page: " + request.getRequestURI());
        userService.addUser(user, roles);
        return "redirect:/users";
    }


}