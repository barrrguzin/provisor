package ru.ptkom.provisor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
public class ConfigFileService {


    private final FileService fileService;
    private final ConfigGeneratorForSNRVP5x configGeneratorForSNRVP5x;
    private final ConfigGeneratorForSPA9xx configGeneratorForSPA9xx;
    private final ApplicationPropertiesFileService applicationPropertiesFileService;


    private static String pathToSPA921InitializingConfigurationFile;
    private static String pathToSPA941InitializingConfigurationFile;
    private static String pathToSNRVP51InitializingConfigurationFile;
    private static String pathToSNRVP52InitializingConfigurationFile;
    private static String pathToSPA9XXConfigurationFileTemplate;
    private static String pathToSNRVP5XConfigurationFileTemplate;
    private static String pathToGeneratedConfigurationFilesToSPA9XX;
    private static String pathToGeneratedConfigurationFilesToSNRVP5X;
    private static String pathToDHCPServiceConfigurationFile;
    private static String pathToTFTPServiceConfigurationFile;


    public ConfigFileService(FileService fileService, ConfigGeneratorForSNRVP5x configGeneratorForSNRVP5x, ConfigGeneratorForSPA9xx configGeneratorForSPA9xx, ApplicationPropertiesFileService applicationPropertiesFileService) {
        this.fileService = fileService;
        this.configGeneratorForSNRVP5x = configGeneratorForSNRVP5x;
        this.configGeneratorForSPA9xx = configGeneratorForSPA9xx;
        this.applicationPropertiesFileService = applicationPropertiesFileService;
        pathToSPA921InitializingConfigurationFile = applicationPropertiesFileService.getPathToSPA921InitializingConfigurationFile();
        pathToSPA941InitializingConfigurationFile = applicationPropertiesFileService.getPathToSPA941InitializingConfigurationFile();
        pathToSNRVP51InitializingConfigurationFile = applicationPropertiesFileService.getPathToSNRVP51InitializingConfigurationFile();
        pathToSNRVP52InitializingConfigurationFile = applicationPropertiesFileService.getPathToSNRVP52InitializingConfigurationFile();
        pathToSPA9XXConfigurationFileTemplate = applicationPropertiesFileService.getPathToSPA9XXConfigurationFileTemplate();
        pathToSNRVP5XConfigurationFileTemplate = applicationPropertiesFileService.getPathToSNRVP5XConfigurationFileTemplate();
        pathToGeneratedConfigurationFilesToSPA9XX = applicationPropertiesFileService.getPathToGeneratedConfigurationFilesToSPA9XX();
        pathToGeneratedConfigurationFilesToSNRVP5X = applicationPropertiesFileService.getPathToGeneratedConfigurationFilesToSNRVP5X();
        pathToDHCPServiceConfigurationFile = applicationPropertiesFileService.getPathToDHCPServiceConfigurationFile();
        pathToTFTPServiceConfigurationFile = applicationPropertiesFileService.getPathToTFTPServiceConfigurationFile();
    }


    public String getDHCPConfigFile() {
        return getConfigFile(pathToDHCPServiceConfigurationFile);
    }


    public void saveDHCPConfigFile(String newConfig) {
        try {
            fileService.writeFile(pathToDHCPServiceConfigurationFile, newConfig);
        } catch (IOException e) {
            log.warn("Не удалось сохранить файл:" + pathToDHCPServiceConfigurationFile);
        }
    }


    public String getTFTPConfigFile() {
        return getConfigFile(pathToTFTPServiceConfigurationFile);
    }


    public void saveTFTPConfigFile(String newConfig) {
        try {
            fileService.writeFile(pathToTFTPServiceConfigurationFile, newConfig);
        } catch (IOException e) {
            log.warn("Не удалось сохранить файл:" + pathToTFTPServiceConfigurationFile);
        }
    }


    public String getConfigFileSPA9XX(String configName) {
        String pathToFile = pathToGeneratedConfigurationFilesToSPA9XX + "/" + configName;
        return getConfigFile(pathToFile);
    }


    public String getConfigFileSNRVP5X(String configName) {
        String pathToFile = pathToGeneratedConfigurationFilesToSNRVP5X + "/" + configName;
        return getConfigFile(pathToFile);
    }


    public String getInitialConfigFileForSPA921() {
        return getConfigFile(pathToSPA921InitializingConfigurationFile);
    }


    public String getInitialConfigFileForSPA941() {
        return getConfigFile(pathToSPA941InitializingConfigurationFile);
    }


    public String getInitialConfigFileForVP51() {
        return getConfigFile(pathToSNRVP51InitializingConfigurationFile);
    }


    public String getInitialConfigFileForVP52() {
        return getConfigFile(pathToSNRVP52InitializingConfigurationFile);
    }


    public String getTemplateConfigFileForSPA9XX() {
        return getConfigFile(pathToSPA9XXConfigurationFileTemplate);
    }


    public void saveTemplateConfigFileForSPA9XX(String newConfig) {
        try {
            fileService.writeFile(pathToSPA9XXConfigurationFileTemplate, newConfig);
        } catch (IOException e) {
            log.warn("Не удалось сохранить файл:" + pathToSPA9XXConfigurationFileTemplate);
        }
    }


    public String getTemplateConfigFileForVP5X() {
        return getConfigFile(pathToSNRVP5XConfigurationFileTemplate);
    }


    public void saveTemplateConfigFileForVP5X(String newConfig) {
        try {
            fileService.writeFile(pathToSNRVP5XConfigurationFileTemplate, newConfig);
        } catch (IOException e) {
            log.warn("Не удалось сохранить файл:" + pathToSNRVP5XConfigurationFileTemplate);
        }
    }


    private String getConfigFile(String pathToFile) {
        String[] configStrings = fileService.openAndRead(pathToFile);
        StringBuilder config = new StringBuilder();
        for (int i = 0; i < configStrings.length; i++) {
            config.append(configStrings[i]).append(System.lineSeparator());
        }
        return config.toString();
    }


    public void defineAndGenerateConfigFile(String number, String mac, String model) {


        if (model.equals("spa9XX")){
            configGeneratorForSPA9xx.generateConfigFile(number, mac);
            log.info("Configuration file to Linksys SPA9XX with number " + number + " and MAC " + mac + " generated!");


        } else if (model.equals("vp5X")) {
            configGeneratorForSNRVP5x.generateConfigFile(number,mac);
            log.info("Configuration file to SNR VP-5X with number " + number + " and MAC " + mac + " generated!");


        } else {
            throw new RuntimeException("Configuration file generator is not define to this phone model");
        }
    }
}