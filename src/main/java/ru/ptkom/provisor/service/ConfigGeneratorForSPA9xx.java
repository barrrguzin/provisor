package ru.ptkom.provisor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.ptkom.provisor.config.PropertiesConfig;
import ru.ptkom.provisor.dao.ECSSUserDataDAO;
import ru.ptkom.provisor.models.sipUsers.OutUsers;


import java.io.IOException;

@Slf4j
@Service
public class ConfigGeneratorForSPA9xx {


    @Autowired
    private ECSSUserDataDAO ecssUserDataDAO;
    @Autowired
    private FileService fileService;


    private static final String PATH_TO_TEMPLATE;
    private static final String PATH_TO_READY_CONFIG_FILES;


    static {
        ApplicationContext context = new AnnotationConfigApplicationContext(PropertiesConfig.class);
        Environment config = context.getEnvironment();
        PATH_TO_TEMPLATE = config.getProperty("path.spa.templ");
        PATH_TO_READY_CONFIG_FILES = config.getProperty("path.spa.confs");
    }



    public void generateConfigFile(String number, String macAddress){


        OutUsers.User aliasData = ecssUserDataDAO.allAliasData(number);


        String login = aliasData.getLogin();
        String password = aliasData.getPassword();
        String username = aliasData.getAliases_list();
        String display_name = aliasData.getAliases_list();
        macAddress = formatMacAddress(macAddress);


        String config = configGenerator(login,password,username,display_name);


        try {
            fileService.writeFile(PATH_TO_READY_CONFIG_FILES + "spa" + macAddress + ".cfg", config);
//            FileWriter configFile = new FileWriter(PATH_TO_READY_CONFIG_FILES + "spa" + macAddress + ".cfg");
//            configFile.write(config);
//            configFile.flush();
//            configFile.close();
            log.info("Configuration file created for SPA9XX (" + PATH_TO_READY_CONFIG_FILES + "spa" + macAddress + ".cfg" + "), with number:" + number + ".");
        } catch (IOException e) {
            log.error("Can't save configuration file for SPA9XX (" + PATH_TO_READY_CONFIG_FILES + "spa" + macAddress + ".cfg" + "). Error: " + e);
            throw new RuntimeException(e);
        }

    }



    private String configGenerator(String login, String password, String username, String display_name) {


        String config = new String();


        String[] template = fileService.openAndRead(PATH_TO_TEMPLATE);


        for (String line : template) {
            if (line.contains("{display_name}")) {
                line = line.replace("{display_name}", display_name);
            }

            if (line.contains("{user_name}")) {
                line = line.replace("{user_name}", username);
            }

            if (line.contains("{password}")) {
                line = line.replace("{password}", password);
            }

            if (line.contains("{login}")) {
                line = line.replace("{login}", login);
            }
            config+= line + "\n";

        }
        return config;
    }


    private String formatMacAddress(String macAddress) {

        macAddress = macAddress.toLowerCase();
        if (macAddress.contains(":")){
            macAddress = macAddress.replaceAll(":","");
        }
        if (macAddress.contains("-")){
            macAddress = macAddress.replaceAll("-","");
        }
        if (macAddress.contains(" ")){
            macAddress = macAddress.replaceAll(" ","");
        }

        return macAddress;
    }
}