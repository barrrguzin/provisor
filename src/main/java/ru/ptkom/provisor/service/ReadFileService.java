package ru.ptkom.provisor.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ptkom.provisor.config.PropertiesConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class ReadFileService {


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
        String[] configStrings = new String[1];

        try {
            configStrings = openAndRead(PATH_TO_READY_CONFIG_FILES + "/" + configName);
        } catch (IOException e) {
            configStrings[0] = "Что-то тут не так...";
        }

        StringBuilder config = new StringBuilder();

        for (int i = 0; i < configStrings.length; i++) {
            config.append(configStrings[i]).append(System.lineSeparator());
        }

        return config.toString();
    }



    private String[] openAndRead(String path_to_template) throws IOException {


        FileReader file = new FileReader(path_to_template);
        BufferedReader varRead = new BufferedReader(file);

        int num = numStrings(path_to_template);
        String[] lines = new String[num];


        for (int i = 0; i < num; i++) {
            lines[i] = varRead.readLine();
        }

        varRead.close();
        return lines;
    }


    private int numStrings(String path_to_file) throws IOException {

        FileReader text = new FileReader(path_to_file);
        BufferedReader y = new BufferedReader(text);

        String one;
        int num = 0;

        while ((one = y.readLine()) != null) {
            num++;
        }
        y.close();

        return num;
    }

}
