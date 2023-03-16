package ru.ptkom.provisor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.ptkom.provisor.client.ECSSAPIClient;
import ru.ptkom.provisor.dao.ECSSUserDataDAO;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.dao.UserDAO;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.User;
import ru.ptkom.provisor.models.exchange.DataToMakeConfig;
import ru.ptkom.provisor.models.exchange.DataToReloadConfig;
import ru.ptkom.provisor.models.sipUsers.OutUsers;
import ru.ptkom.provisor.service.*;


import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;


@Controller
public class IndexController {


    private static String[] LIST_OF_MODELS_OF_PHONES_IN_COMPANY= {"spa9XX","vp5X","grandstream"};
    private static String[] LIST_OF_SUPPORTED_ROLES = {"ROLE_SUPERADMIN", "ROLE_ADMIN", "ROLE_USER"};
    private static String SERVER_ADDRESS = "192.168.68.2";


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

    @Autowired
    @Qualifier("RestTemplateWithDigestAuth")
    private RestTemplate restTemplate;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ConfigFileService configFileService;

    @Autowired
    private NetworkService networkService;


    @GetMapping("/monitor")
    public String testArt(Model model){
        long time = System.currentTimeMillis();
        String[][] ipAndMacAddresses = networkService.showActiveHosts("192.168.68.0");
        String[] macs = new String[ipAndMacAddresses.length];
        for (int i = 0; i < ipAndMacAddresses.length; i++) {
            if (ipAndMacAddresses[i][1] == null) {
                macs[i] = "";
            } else {
                macs[i] = ipAndMacAddresses[i][1];
            }

        }


        long base = System.currentTimeMillis();
        String[][] userData = pbxUserDAO.findByMacList(macs);
        System.out.println("Base:" + (System.currentTimeMillis() - base)/1000f);
        if (ipAndMacAddresses.length != 0) {
            String[][] result = new String[ipAndMacAddresses.length][ipAndMacAddresses[0].length + userData[0].length];

            for (int i = 0; i < result.length; i++) {
                result[i][0] = ipAndMacAddresses[i][0];
                result[i][2] = userData[i][0];
                result[i][3] = userData[i][1];
                result[i][4] = userData[i][2];
                if (userData[i][2].equals("vp5X")) {
                    //result[i][1] = ipAndMacAddresses[i][1].toUpperCase();
                    result[i][1] = ipAndMacAddresses[i][1];
                } else {
                    result[i][1] = ipAndMacAddresses[i][1];
                }
            }
            System.out.println("Time:" + (System.currentTimeMillis() - time) / 1000f);

            model.addAttribute("hosts", result);
            return "network/network";
        } else {
            String[][] result = new String[1][1];
            result[0][0] = "IP адресов в сети не обнаружено";
            model.addAttribute("hosts", result);
            return "network/network";
        }

    }


    @PatchMapping("/reload")
    public @ResponseBody String testDigest(@ModelAttribute("data") DataToReloadConfig data) {

        String phoneIp = data.getIp();
        String phoneMac = data.getMac();
        String phoneModel = data.getModel();
        String configUrl = new String();
        if (phoneModel.equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[0])) {
            configUrl = "http://" + SERVER_ADDRESS + "/linksys/spa" + phoneMac + ".cfg";
        } if (phoneModel.equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[1])) {
            configUrl = "http://" + SERVER_ADDRESS + "/snrvp/" + phoneMac.toUpperCase() + ".cfg";
        }


        String finalUrl = "http://" + phoneIp + "/admin/resync?" + configUrl;

        System.out.println(finalUrl);

        ResponseEntity entity = restTemplate.exchange(finalUrl, HttpMethod.GET, null, String.class);

        return finalUrl + "\n" + entity.getBody().toString();
    }


    @GetMapping("/basic")
    public String basicToMenuRedirect(){
        return "redirect:/";
    }


    @GetMapping("/linksys/{configName}")
    public @ResponseBody String addressesControl(@PathVariable("configName") String configName, HttpServletRequest request){

        String ip = requestService.getClientIp(request);
        String mac = configName.replace("spa", "").replace(".cfg", "");
        String output = "Конфиг получен: IP: " + ip + ", MAC: " + mac;
        String response = configFileService.getConfigFile(configName);
        System.out.println(output);

        return response;

    }




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

        String result = "Произошла ошибка(";

            if(number.equals("")){
                result = "Вы бы ввели хоть что-нибудь";
                model.addAttribute("result", result);
                return "phone/make";
            }
            String mac = pbxUserDAO.getMacByNumber(number);
            String phoneModel = pbxUserDAO.getPhoneModelByNumber(number);

            if(mac == null || phoneModel == null){
                result = "Пользователь с данным номером не найден";
                model.addAttribute("result", result);
                return "phone/make";
            }

            try {
                if (phoneModel.equals("spa9XX")){
                    configGeneratorForSPA9xx.generateConfigFile(number, mac);
                    result = "Success!";
                    model.addAttribute("result", result);
                    return "phone/make";
                }
                if (phoneModel.equals("vp5X")){
                    configGeneratorForSNRVP5x.generateConfigFile(number,mac);
                    result = "Success!";
                    model.addAttribute("result", result);
                    return "phone/make";
                }
                else {
                    result = "Генератор конфига для данного ТА не существует(";
                    model.addAttribute("result", result);
                    return "phone/make";
                }
            } catch (Exception e) {
                result = e.toString();
                model.addAttribute("result", result);
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