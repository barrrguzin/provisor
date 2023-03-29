package ru.ptkom.provisor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ptkom.provisor.config.PropertiesConfig;

import java.io.FileWriter;
import java.io.IOException;


@Slf4j
@Service
public class ConfigFileService {


    @Autowired
    private FileService fileService;
    @Autowired
    private ConfigGeneratorForSNRVP5x configGeneratorForSNRVP5x;
    @Autowired
    private ConfigGeneratorForSPA9xx configGeneratorForSPA9xx;


    private static final String PATH_TO_SPA921_INIT_CONFIG;
    private static final String PATH_TO_SPA941_INIT_CONFIG;
    private static final String PATH_TO_VP51_INIT_CONFIG;
    private static final String PATH_TO_VP52_INIT_CONFIG;
    private static final String PATH_TO_SPA9XX_TEMPLATE;
    private static final String PATH_TO_VP5X_TEMPLATE;
    private static final String PATH_TO_READY_SPA9XX_CONFIG_FILES;
    private static final String PATH_TO_READY_SNRVP5X_CONFIG_FILES;
    private static final String PATH_TO_DHCP_CONFIG_FILE;
    private static final String PATH_TO_TFTP_CONFIG_FILE;

    static {
        ApplicationContext context = new AnnotationConfigApplicationContext(PropertiesConfig.class);
        Environment config = context.getEnvironment();
        PATH_TO_SPA9XX_TEMPLATE = config.getProperty("path.spa.templ");
        PATH_TO_VP5X_TEMPLATE = config.getProperty("path.snrvp.templ");
        PATH_TO_READY_SPA9XX_CONFIG_FILES = config.getProperty("path.spa.confs");
        PATH_TO_READY_SNRVP5X_CONFIG_FILES = config.getProperty("path.snrvp.confs");
        PATH_TO_SPA921_INIT_CONFIG = config.getProperty("path.spa921.init");
        PATH_TO_SPA941_INIT_CONFIG = config.getProperty("path.spa941.init");
        PATH_TO_VP51_INIT_CONFIG = config.getProperty("path.snrvp51.init");
        PATH_TO_VP52_INIT_CONFIG = config.getProperty("path.snrvp52.init");
        PATH_TO_DHCP_CONFIG_FILE = config.getProperty("dhcp.service.configuration.path");
        PATH_TO_TFTP_CONFIG_FILE = config.getProperty("tftp.service.configuration.path");
    }


    public String getDHCPConfigFile() {
        return getConfigFile(PATH_TO_DHCP_CONFIG_FILE);
    }


    public void saveDHCPConfigFile(String newConfig) {
        try {
            fileService.writeFile(PATH_TO_DHCP_CONFIG_FILE, newConfig);
        } catch (IOException e) {
            log.warn("Не удалось сохранить файл:" + PATH_TO_DHCP_CONFIG_FILE);
        }
    }


    public String getTFTPConfigFile() {
        return getConfigFile(PATH_TO_TFTP_CONFIG_FILE);
    }


    public void saveTFTPConfigFile(String newConfig) {
        try {
            fileService.writeFile(PATH_TO_TFTP_CONFIG_FILE, newConfig);
        } catch (IOException e) {
            log.warn("Не удалось сохранить файл:" + PATH_TO_TFTP_CONFIG_FILE);
        }
    }


    public String getConfigFileSPA9XX(String configName) {
        String pathToFile = PATH_TO_READY_SPA9XX_CONFIG_FILES + "/" + configName;
        return getConfigFile(pathToFile);
    }


    public String getConfigFileSNRVP5X(String configName) {
        String pathToFile = PATH_TO_READY_SNRVP5X_CONFIG_FILES + "/" + configName;
        return getConfigFile(pathToFile);
    }


    public String getInitialConfigFileForSPA921() {
        return getConfigFile(PATH_TO_SPA921_INIT_CONFIG);
    }


    public String getInitialConfigFileForSPA941() {
        return getConfigFile(PATH_TO_SPA941_INIT_CONFIG);
    }


//    public void saveInitialConfigFileForSPA9XX(String newConfig) {
//        try {
//            fileService.writeFile(PATH_TO_SPA9XX_INIT_CONFIG, newConfig);
//        } catch (IOException e) {
//            log.warn("Не удалось сохранить файл:" + PATH_TO_SPA9XX_INIT_CONFIG);
//        }
//    }


    public String getInitialConfigFileForVP51() {
        return getConfigFile(PATH_TO_VP51_INIT_CONFIG);
    }


    public String getInitialConfigFileForVP52() {
        return getConfigFile(PATH_TO_VP52_INIT_CONFIG);
    }


//    public void saveInitialConfigFileForVP5X(String newConfig) {
//        try {
//            fileService.writeFile(PATH_TO_VP5X_INIT_CONFIG, newConfig);
//        } catch (IOException e) {
//            log.warn("Не удалось сохранить файл:" + PATH_TO_VP5X_INIT_CONFIG);
//        }
//    }


    public String getTemplateConfigFileForSPA9XX() {
        return getConfigFile(PATH_TO_SPA9XX_TEMPLATE);
    }


    public void saveTemplateConfigFileForSPA9XX(String newConfig) {
        try {
            fileService.writeFile(PATH_TO_SPA9XX_TEMPLATE, newConfig);
        } catch (IOException e) {
            log.warn("Не удалось сохранить файл:" + PATH_TO_SPA9XX_TEMPLATE);
        }
    }


    public String getTemplateConfigFileForVP5X() {
        return getConfigFile(PATH_TO_VP5X_TEMPLATE);
    }


    public void saveTemplateConfigFileForVP5X(String newConfig) {
        try {
            fileService.writeFile(PATH_TO_VP5X_TEMPLATE, newConfig);
        } catch (IOException e) {
            log.warn("Не удалось сохранить файл:" + PATH_TO_VP5X_TEMPLATE);
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


    public String defineAndGenerateConfigFile(String number, String mac, String model) {


        if (model.equals("spa9XX")){
            configGeneratorForSPA9xx.generateConfigFile(number, mac);
            return "Configuration file to Linksys SPA9XX with number " + number + " and MAC " + mac + " generated!";


        } else if (model.equals("vp5X")) {
            configGeneratorForSNRVP5x.generateConfigFile(number,mac);
            return "Configuration file to SNR VP-5X with number " + number + " and MAC " + mac + " generated!";


        } else {
            return "Phone model is not define!";
        }
    }


}