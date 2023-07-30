package ru.ptkom.provisor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.ptkom.provisor.models.exchange.DataToReloadConfig;
import ru.ptkom.provisor.service.ApplicationPropertiesFileService;
import ru.ptkom.provisor.service.ConfigFileService;
import ru.ptkom.provisor.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@RestController
public class ProvisionController {


    private final RestTemplate digestRestTemplate;
    private final RequestService requestService;
    private final ConfigFileService configFileService;
    private final ApplicationPropertiesFileService applicationPropertiesFileService;


    private static final String[] LIST_OF_MODELS_OF_PHONES_IN_COMPANY= {"spa9XX","vp5X","grandstream"};
    private static String hostIPAddress;

    public ProvisionController(@Qualifier("RestTemplateWithDigestAuth") RestTemplate digestRestTemplate, RequestService requestService, ConfigFileService configFileService, ApplicationPropertiesFileService applicationPropertiesFileService) {
        this.digestRestTemplate = digestRestTemplate;
        this.requestService = requestService;
        this.configFileService = configFileService;
        this.applicationPropertiesFileService = applicationPropertiesFileService;
        hostIPAddress = applicationPropertiesFileService.getHostIPAddress();
    }


    @GetMapping("/linksys/{configName}")
    public String giveConfigToSPA9XX(@PathVariable("configName") String configName, HttpServletRequest request){
        String ip = requestService.getClientIp(request);
        String mac = configName.replace("spa", "").replace(".cfg", "");

        log.info("Request configuration file for Lynksys SPA9XX by: " + ip + "; " + mac + ";");

        //String output = "Запрос на получение конфига от: IP: " + ip + ", MAC: " + mac;
        String configuration = configFileService.getConfigFileSPA9XX(configName);


        return configuration;
    }


    @GetMapping("/snr-vp/{configName}")
    public String giveConfigToSNRVP5X(@PathVariable("configName") String configName, HttpServletRequest request){
        String ip = requestService.getClientIp(request);
        String mac = configName.replace(".cfg", "");

        log.info("Request configuration file for SNR VP-5X by: " + ip + "; " + mac + ";");

        String configuration = configFileService.getConfigFileSNRVP5X(configName);


        return configuration;
    }


    @PatchMapping("/reload")
    public ResponseEntity requestReloadFromSPA9XX(@RequestBody DataToReloadConfig data, Principal principal, HttpServletRequest request) {

        String phoneMac = data.getMac();
        String phoneModel = data.getModel();
        String configUrl = new String();


        if (phoneModel.equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[0])) {
            configUrl = "http://" + hostIPAddress + "/linksys/spa" + phoneMac + ".cfg";
        } if (phoneModel.equals(LIST_OF_MODELS_OF_PHONES_IN_COMPANY[1])) {
            configUrl = "http://" + hostIPAddress + "/snrvp/" + phoneMac.toUpperCase() + ".cfg";
        }


        String finalUrl = "http://" + data.getIp() + "/admin/resync?" + configUrl;
        ResponseEntity entity = digestRestTemplate.exchange(finalUrl, HttpMethod.GET, null, String.class);

        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to send PATCH request to page: " + request.getRequestURI() + "; Status code: " + entity.getStatusCode());

        return entity;
    }


}