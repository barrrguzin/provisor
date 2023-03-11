package ru.ptkom.provisor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.client.ECSSAPIClient;
import ru.ptkom.provisor.dao.ECSSUserDataDAO;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.dao.UserDAO;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.models.sipUsers.OutUsers;
import ru.ptkom.provisor.service.ConfigGeneratorForSNRVP5x;
import ru.ptkom.provisor.service.ConfigGeneratorForSPA9xx;
import ru.ptkom.provisor.service.UserService;


import java.security.Principal;
import java.util.List;
import java.util.Map;


@Controller
public class IndexController {


    private static String[] LIST_OF_MODELS_OF_PHONES_IN_COMPANY= {"spa9XX","vp5X","grandstream"};
    private static String[] LIST_OF_SUPPORTED_ROLES = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_USER"};


    @Autowired
    private ECSSUserDataDAO ecssUserDataDAO;
    @Autowired
    private PBXUserDAO pbxUserDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ECSSAPIClient ecssapiClient;
    @Autowired
    private ConfigGeneratorForSPA9xx configGeneratorForSPA9xx;
    @Autowired
    private ConfigGeneratorForSNRVP5x configGeneratorForSNRVP5x;
    @Autowired
    private UserService userService;


    @GetMapping("/video")
    public String testVideo() {
        return "video";
    }





    @GetMapping("/")
    public String defaultPage(Model model, Principal principal) {
        String name = principal.getName();
        model.addAttribute("name", name);
        return "navigation/menu";
    }


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


    @GetMapping("/aliases")
    public String getAliases(Model model) {
        List<List<String>> aliases = ecssUserDataDAO.listOfAliasesNames(ecssapiClient.getListOfAliases());
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


    @GetMapping("/spa9xx")
    public String toCiscoSPA9xx(String name) {
        return "phone/linksys/spa9xx/spa9xx";
    }


    @GetMapping("/config/make")
    public String takeDataToMakeConfigToSPA9xx(Model model) {
        model.addAttribute("number", "");
        model.addAttribute("result", "");
        return "phone/make";
    }


    @PostMapping("/config/make")
    public String makeConfigPhoneApparation(@RequestParam String number, Model model) {

            if(number.equals("")){
                model.addAttribute("result", "Вы бы ввели хоть что-нибудь");
                return "phone/make";
            }
            String mac = pbxUserDAO.getMacByNumber(number);
            String phoneModel = pbxUserDAO.getPhoneModelByNumber(number);

            if(mac == null || phoneModel == null){
                model.addAttribute("result", "Пользователь с данным номером не найден");
                return "phone/make";
            }

            if (phoneModel.equals("spa9XX")){
                configGeneratorForSPA9xx.generateConfigFile(number, mac);
                model.addAttribute("result", "Success!");
                return "phone/make";
            }
            if (phoneModel.equals("vp5X")){
                configGeneratorForSNRVP5x.generateConfigFile(number,mac);
                model.addAttribute("result", "Success!");
                return "phone/make";
            }
            else {
                model.addAttribute("result", "Генератор конфига для данного ТА не существует(");
                return "phone/make";
            }
        }


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