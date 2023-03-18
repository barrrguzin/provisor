package ru.ptkom.provisor.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.service.ConfigGeneratorForSNRVP5x;
import ru.ptkom.provisor.service.ConfigGeneratorForSPA9xx;

@Controller
public class ConfigurationFilesController {


    @Autowired
    private PBXUserDAO pbxUserDAO;
    @Autowired
    private ConfigGeneratorForSPA9xx configGeneratorForSPA9xx;
    @Autowired
    private ConfigGeneratorForSNRVP5x configGeneratorForSNRVP5x;


    @GetMapping("/config/make")
    public String takeDataToMakeConfigToSPA9xx(Model model) {
        model.addAttribute("number", "");
        model.addAttribute("result", "");
        return "phone/make";
    }


    @PostMapping("/config/make")
    public @ResponseBody String makeConfigPhoneApparation(@RequestParam String number, Model model) {


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




}