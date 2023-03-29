package ru.ptkom.provisor.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.ptkom.provisor.service.ConfigFileService;
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
    @Autowired
    private ConfigFileService configFileService;

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


    @GetMapping("/server/services/dhcp")
    public String getDHCPServiceConfigurationFile(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String dhcpConfiguration = configFileService.getDHCPConfigFile();
        model.addAttribute("dhcpConfiguration", dhcpConfiguration);
        return "server/dhcp";
    }


    @GetMapping("/server/services/dhcp/edit")
    public String getFormWithDHCPServiceConfigurationFile(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String dhcpConfiguration = configFileService.getDHCPConfigFile();
        model.addAttribute("dhcpConfiguration", dhcpConfiguration);
        return "server/dhcpEditConfig";
    }


    @PatchMapping("/server/services/dhcp/edit")
    public String updateDHCPConfigFile(@RequestParam String dhcpConfiguration, Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName()
                + "; From: " + requestService.getClientIp(request)
                + "; Try to send PATCH request: " + request.getRequestURI());
        configFileService.saveDHCPConfigFile(dhcpConfiguration);
        return "redirect:/server/services/dhcp";
    }


    @GetMapping("/server/services/tftp")
    public String getTFTPServiceConfigurationFile(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String tftpConfiguration = configFileService.getTFTPConfigFile();
        model.addAttribute("tftpConfiguration", tftpConfiguration);
        return "server/tftp";
    }


    @GetMapping("/server/services/tftp/edit")
    public String getFormWithTFTPServiceConfigurationFile(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String tftpConfiguration = configFileService.getTFTPConfigFile();
        model.addAttribute("tftpConfiguration", tftpConfiguration);
        return "server/tftpEditConfig";
    }


    @PatchMapping("/server/services/tftp/edit")
    public String updateTFTPConfigFile(@RequestParam String tftpConfiguration, Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName()
                + "; From: " + requestService.getClientIp(request)
                + "; Try to send PATCH request: " + request.getRequestURI());
        //serverServicesManagerService.stopTFTPService();
        configFileService.saveTFTPConfigFile(tftpConfiguration);
        //serverServicesManagerService.startTFTPService();
        return "redirect:/server/services/tftp";
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


    @GetMapping("/server/configurations")
    public String initAndTemplateConfigurationsMenu(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        return "server/configurations";
    }


    @GetMapping("/server/configurations/spa921/init")
    public String getInitialConfigFileForSPA921(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String initSPA = configFileService.getInitialConfigFileForSPA921();
        model.addAttribute("initSPA", initSPA);
        return "phone/spa9xx/init";
    }


    @GetMapping("/server/configurations/spa941/init")
    public String getInitialConfigFileForSPA941(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String initSPA = configFileService.getInitialConfigFileForSPA941();
        model.addAttribute("initSPA", initSPA);
        return "phone/spa9xx/init";
    }


    @GetMapping("/server/configurations/spa9xx/template")
    public String getTemplateConfigFileForSPA9XX(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String templateSPA = configFileService.getTemplateConfigFileForSPA9XX();
        model.addAttribute("templateSPA", templateSPA);
        return "phone/spa9xx/template";
    }


    @GetMapping("/server/configurations/vp51/init")
    public String getInitialConfigFileForVP51(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String initSNR = configFileService.getInitialConfigFileForVP51();
        model.addAttribute("initSNR", initSNR);
        return "phone/vp5x/init";
    }


    @GetMapping("/server/configurations/vp52/init")
    public String getInitialConfigFileForVP52(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String initSNR = configFileService.getInitialConfigFileForVP52();
        model.addAttribute("initSNR", initSNR);
        return "phone/vp5x/init";
    }


    @GetMapping("/server/configurations/vp5x/template")
    public String getTemplateConfigFileForVP5X(Model model, Principal principal, HttpServletRequest request) {
        log.info("User: " + principal.getName() + "; From: " + requestService.getClientIp(request) + "; Try to get page: " + request.getRequestURI());
        String templateSNR = configFileService.getTemplateConfigFileForVP5X();
        model.addAttribute("templateSNR", templateSNR);
        return "phone/vp5x/template";
    }
}