package ru.ptkom.provisor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.config.PropertiesConfig;
import ru.ptkom.provisor.dao.ECSSUserDataDAO;
import ru.ptkom.provisor.models.sipUsers.OutUsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class ConfigGeneratorForSNRVP5x {


    @Autowired
    private ECSSUserDataDAO ecssUserDataDAO;
    @Autowired
    private FileService fileService;


    private static final String PATH_TO_INIT_CONFIG;
    private static final String PATH_TO_TEMPLATE;
    private static final String PATH_TO_READY_CONFIG_FILES;


    static {
        //works
        ApplicationContext context = new AnnotationConfigApplicationContext(PropertiesConfig.class);
        Environment config = context.getEnvironment();

        PATH_TO_TEMPLATE = config.getProperty("path.snrvp.templ");
        PATH_TO_READY_CONFIG_FILES = config.getProperty("path.snrvp.confs");
        PATH_TO_INIT_CONFIG = config.getProperty("path.snrvp.init");
    }



    public void generateConfigFile(String number, String mac_address){


        OutUsers.User aliasData = ecssUserDataDAO.allAliasData(number);

        String login = aliasData.getLogin();
        String password = aliasData.getPassword();
        String username = aliasData.getAliases_list();
        String display_name = aliasData.getAliases_list();


        String config = configGenerator(login,password,username,display_name);


        if (mac_address.contains(":")){
            mac_address = mac_address.replace(":","");
        }
        if (mac_address.contains("-")){
            mac_address = mac_address.replace("-","");
        }
        if (mac_address.contains(" ")){
            mac_address = mac_address.replace(" ","");
        }
        mac_address = mac_address.toUpperCase();

        try {
            FileWriter configFile = new FileWriter(PATH_TO_READY_CONFIG_FILES + mac_address + ".cfg");
            configFile.write(config);
            configFile.flush();
            configFile.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    public String configGenerator(String login, String password, String username, String display_name) {
        String[] template = new String[0];
        String config = new String();


        try {
            template = fileService.openAndRead(PATH_TO_TEMPLATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


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
}
