package ru.ptkom.provisor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.UserDAO;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.service.UserService;

import java.util.Map;

@Controller
public class UsersController {


    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserService userService;


    private static String[] LIST_OF_SUPPORTED_ROLES = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_USER"};


    @GetMapping("/users")
    public String usersShow(Map<String, Object> model) {
        Iterable<User> users = userDAO.getAllUsers();
        model.put("users", users);
        return "users/users/show";
    }


    @GetMapping("/users/{id}")
    public String showUserParameters(@PathVariable("id") Long id, Model model) {
        User user = userDAO.getUserById(id);
        model.addAttribute("user", user);
        return "users/users/user";
    }


    @GetMapping("/users/{id}/edit")
    public String editUserParameters(@PathVariable("id") Long id, Model model) {
        User user = userDAO.getUserById(id);
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("list", LIST_OF_SUPPORTED_ROLES);
        if (user.getUsername().equals("barguzin")){
            return "users/users/userDefault";
        } else {
            return "users/users/edit";
        }
    }


    @PatchMapping("/users/edit")
    public String updateUsersData(@ModelAttribute User user, @RequestParam(name = "rolesList", defaultValue = "empty") String[] roles, Model model){
        if (roles[0].equals("empty")){
            model.addAttribute("user", user);
            model.addAttribute("list", LIST_OF_SUPPORTED_ROLES);
            model.addAttribute("result", "Выберете хотябы одну роль!");
            return "users/users/edit";
        }
        String number = user.getPbxUser().getNumber();
        if (number.equals("")){
            userService.updateUser(user, roles);

        } else {
            userService.updateUser(user,number, roles);

        }
        return "redirect:/users";
    }


    @DeleteMapping("/users/delete")
    public String deleteUsersData(@ModelAttribute("user") User user){
        if (user.getUsername() != null) {
            if (user.getUsername().equals("barguzin")) {
                return "redirect:/users";
            } else {
                userDAO.deleteUser(user);
                return "redirect:/users";
            }
        }
        return "redirect:/users";
    }


    @GetMapping("/users/add")
    public String addUsersForm(Map<String, Object> model) {
        model.put("user", new User());
        model.put("list", LIST_OF_SUPPORTED_ROLES);
        return "users/users/add";
    }


    @PostMapping("/users/add")
    public String addUser(@ModelAttribute User user, @RequestParam("rolesList") String[] roles){
        userService.addUser(user, roles);
        return "redirect:/users";
    }


}