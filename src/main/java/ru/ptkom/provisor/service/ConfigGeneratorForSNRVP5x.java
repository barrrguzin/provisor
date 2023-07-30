package ru.ptkom.provisor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ptkom.provisor.dao.ECSSUserDataDAO;
import ru.ptkom.provisor.models.sip_user_show.UserType;


import java.io.IOException;

@Slf4j
@Service
public class ConfigGeneratorForSNRVP5x {


    private final ECSSUserDataDAO ecssUserDataDAO;
    private final FileService fileService;
    private final ApplicationPropertiesFileService applicationPropertiesFileService;


    private static String pathToSNRVP5XConfigurationFileTemplate;
    private static String pathToGeneratedConfigurationFilesToSNRVP5X;


    public ConfigGeneratorForSNRVP5x(ECSSUserDataDAO ecssUserDataDAO, FileService fileService, ApplicationPropertiesFileService applicationPropertiesFileService) {
        this.ecssUserDataDAO = ecssUserDataDAO;
        this.fileService = fileService;
        this.applicationPropertiesFileService = applicationPropertiesFileService;
        pathToSNRVP5XConfigurationFileTemplate = applicationPropertiesFileService.getPathToSNRVP5XConfigurationFileTemplate();
        pathToGeneratedConfigurationFilesToSNRVP5X = applicationPropertiesFileService.getPathToGeneratedConfigurationFilesToSNRVP5X();
    }


    public void generateConfigFile(String number, String macAddress){


        UserType aliasData = ecssUserDataDAO.allAliasData(number);


        String login = aliasData.getLogin();
        String password = aliasData.getPassword();
        String username = aliasData.getAliasesList();
        String display_name = aliasData.getAliasesList();


        String config = configGenerator(login,password,username,display_name);


        macAddress = formatMacAddress(macAddress);


        try {
            fileService.writeFile(pathToGeneratedConfigurationFilesToSNRVP5X + macAddress + ".cfg", config);
            log.info("Configuration file created for VP5X (" + pathToGeneratedConfigurationFilesToSNRVP5X + macAddress + ".cfg" + "), with number:" + number + ".");

        } catch (IOException e) {
            log.error("Can't save configuration file for VP5X (" + pathToGeneratedConfigurationFilesToSNRVP5X + macAddress + ".cfg" + "). Error: " + e);
            throw new RuntimeException(e);

        }
    }



    private String configGenerator(String login, String password, String username, String display_name) {

        String config = new String();

        String[] template = fileService.openAndRead(pathToSNRVP5XConfigurationFileTemplate);

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

        macAddress = macAddress.toUpperCase();
        if (macAddress.contains(":")){
            macAddress = macAddress.replace(":","");
        }
        if (macAddress.contains("-")){
            macAddress = macAddress.replace("-","");
        }
        if (macAddress.contains(" ")){
            macAddress = macAddress.replace(" ","");
        }
        return macAddress;
    }


}