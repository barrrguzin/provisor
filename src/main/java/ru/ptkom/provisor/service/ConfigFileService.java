package ru.ptkom.provisor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ptkom.provisor.config.PropertiesConfig;


@Service
public class ConfigFileService {

    @Autowired
    private FileService fileService;


    private static final String PATH_TO_INIT_CONFIG;
    private static final String PATH_TO_TEMPLATE;
    private static final String PATH_TO_READY_CONFIG_FILES;


    static {
        //works
        ApplicationContext context = new AnnotationConfigApplicationContext(PropertiesConfig.class);
        Environment config = context.getEnvironment();

        PATH_TO_TEMPLATE = config.getProperty("path.spa.templ");
        PATH_TO_READY_CONFIG_FILES = config.getProperty("path.spa.confs");
        PATH_TO_INIT_CONFIG = config.getProperty("path.spa.init");
    }



    public String getConfigFile(String configName) {


        String[] configStrings = fileService.openAndRead(PATH_TO_READY_CONFIG_FILES + "/" + configName);


        StringBuilder config = new StringBuilder();


        for (int i = 0; i < configStrings.length; i++) {
            config.append(configStrings[i]).append(System.lineSeparator());
        }

        return config.toString();
    }
}