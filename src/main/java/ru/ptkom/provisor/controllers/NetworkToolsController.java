package ru.ptkom.provisor.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.service.NetworkService;


@Controller
public class NetworkToolsController {


    @Autowired
    private NetworkService networkService;
    @Autowired
    private PBXUserDAO pbxUserDAO;


    private static String SERVER_ADDRESS = "192.168.68.2";


    @GetMapping("/monitor")
    public String testArt(Model model) {
        String[][] ipAndMacAddresses = networkService.showActiveHosts(SERVER_ADDRESS);
        String[] macs = new String[ipAndMacAddresses.length];


        for (int i = 0; i < ipAndMacAddresses.length; i++) {
            if (ipAndMacAddresses[i][1] == null) {
                macs[i] = "";
            } else {
                macs[i] = ipAndMacAddresses[i][1];
            }
        }


        String[][] userData = pbxUserDAO.findByMacList(macs);
        if (ipAndMacAddresses.length != 0) {
            String[][] result = new String[ipAndMacAddresses.length][ipAndMacAddresses[0].length + userData[0].length];


            for (int i = 0; i < result.length; i++) {
                result[i][0] = ipAndMacAddresses[i][0];
                result[i][2] = userData[i][0];
                result[i][3] = userData[i][1];
                result[i][4] = userData[i][2];
                if (userData[i][2].equals("vp5X")) {
                    result[i][1] = ipAndMacAddresses[i][1];
                } else {
                    result[i][1] = ipAndMacAddresses[i][1];
                }
            }
            model.addAttribute("hosts", result);
            return "network/network";
        } else {
            String[][] result = new String[1][1];
            result[0][0] = "IP адресов в сети не обнаружено";
            model.addAttribute("hosts", result);
            return "network/network";
        }
    }


}