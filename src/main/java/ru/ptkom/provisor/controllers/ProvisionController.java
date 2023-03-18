package ru.ptkom.provisor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.ptkom.provisor.models.exchange.DataToReloadConfig;
import ru.ptkom.provisor.service.ConfigFileService;
import ru.ptkom.provisor.service.RequestService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProvisionController {


    @Autowired
    @Qualifier("RestTemplateWithDigestAuth")
    private RestTemplate digestRestTemplate;
    @Autowired
    private RequestService requestService;
    @Autowired
    private ConfigFileService configFileService;


    private static String[] LIST_OF_MODELS_OF_PHONES_IN_COMPANY= {"spa9XX","vp5X","grandstream"};
    private static String SERVER_ADDRESS = "192.168.68.2";




    @GetMapping("/linksys/{configName}")
    public String addressesControl(@PathVariable("configName") String configName, HttpServletRequest request){


        String ip = requestService.getClientIp(request);
        String mac = configName.replace("spa", "").replace(".cfg", "");
        //String output = "Запрос на получение конфига от: IP: " + ip + ", MAC: " + mac;
        String configuration = configFileService.getConfigFile(configName);


        return configuration;
    }


    @PatchMapping("/reload")
    public String testDigest(@ModelAttribute("data") DataToReloadConfig data) {


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
        ResponseEntity entity = digestRestTemplate.exchange(finalUrl, HttpMethod.GET, null, String.class);


        return finalUrl + "\n" + entity.getBody().toString();
    }


}