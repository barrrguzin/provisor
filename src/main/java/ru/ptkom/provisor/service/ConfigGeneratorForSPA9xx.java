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
public class ConfigGeneratorForSPA9xx {


    @Autowired
    private ECSSUserDataDAO ecssUserDataDAO;


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
        mac_address = mac_address.toLowerCase();

        try {
            FileWriter configFile = new FileWriter(PATH_TO_READY_CONFIG_FILES + "spa" + mac_address + ".cfg");
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
            template = openAndRead(PATH_TO_TEMPLATE);
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

        //byte[] bytes = config.getBytes(StandardCharsets.UTF_8);
        //String configUTF8 = new String(bytes, StandardCharsets.UTF_8);
        //System.out.println(configUTF8);

        return config;




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
