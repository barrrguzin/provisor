package ru.ptkom.provisor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.ptkom.provisor.service.RequestService;
import ru.ptkom.provisor.service.ServerServicesManagerService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@Controller
public class ServerController {

    @Autowired
    private RequestService requestService;
    @Autowired
    private ServerServicesManagerService serverServicesManagerService;

    @GetMapping("/server")
    public String serverMenu(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        return "navigation/serverMenu";
    }


    @GetMapping("/server/services")
    public String services(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());

        String tftpStatus = serverServicesManagerService.getTFTPServiceStatus();
        String dhcpStatus = serverServicesManagerService.getDHCPServiceStatus();

        model.addAttribute("tftpStatus", tftpStatus);
        model.addAttribute("dhcpStatus", dhcpStatus);

        return "server/services";
    }


    @PostMapping("/server/services/restart")
    public @ResponseBody String restartService(@RequestParam String service, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName()
                + "; From: " + requestService.getClientIp(request)
                + "; Try to send POST request: " + request.getRequestURI());

        if (service.equals("tftp")) {
            String result = serverServicesManagerService.restartTFTPService();
            return result;
        } else if (service.equals("dhcp")) {
            String result = serverServicesManagerService.restartDHCPService();
            return result;
        } else {
            return service;
        }
    }


    @PostMapping("/server/services/start")
    public @ResponseBody String start(@RequestParam String service, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName()
                + "; From: " + requestService.getClientIp(request)
                + "; Try to send POST request: " + request.getRequestURI());

        if (service.equals("tftp")) {
            String result = serverServicesManagerService.startTFTPService();
            return result;
        } else if (service.equals("dhcp")) {
            String result = serverServicesManagerService.startDHCPService();
            return result;
        } else {
            return service;
        }
    }


    @PostMapping("/server/services/stop")
    public @ResponseBody String stopService(@RequestParam String service, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName()
                + "; From: " + requestService.getClientIp(request)
                + "; Try to send POST request: " + request.getRequestURI());

        if (service.equals("tftp")) {
            String result = serverServicesManagerService.stopTFTPService();
            return result;
        } else if (service.equals("dhcp")) {
            String result = serverServicesManagerService.stopDHCPService();
            return result;
        } else {
            return service;
        }
    }


    }