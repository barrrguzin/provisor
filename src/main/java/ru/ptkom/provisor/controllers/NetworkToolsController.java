package ru.ptkom.provisor.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.ptkom.provisor.service.ApplicationPropertiesFileService;
import ru.ptkom.provisor.service.NetworkService;
import ru.ptkom.provisor.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


@Slf4j
@Controller
public class NetworkToolsController {


    private final NetworkService networkService;
    private final RequestService requestService;
    private final ApplicationPropertiesFileService applicationPropertiesFileService;


    private static String hostIPAddress;


    public NetworkToolsController(NetworkService networkService, RequestService requestService, ApplicationPropertiesFileService applicationPropertiesFileService) {
        this.networkService = networkService;
        this.requestService = requestService;
        this.applicationPropertiesFileService = applicationPropertiesFileService;
        hostIPAddress = applicationPropertiesFileService.getHostIPAddress();
    }


    @GetMapping(value = "/monitor", produces = "application/json")
    public ResponseEntity scanNetwork(Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        return ResponseEntity.ok(networkService.scanNetworkAndSetResult(hostIPAddress));
    }


}