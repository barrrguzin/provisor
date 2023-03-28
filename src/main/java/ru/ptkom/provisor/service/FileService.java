package ru.ptkom.provisor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class FileService {


    public String[] openAndRead(String pathToFile) {

        String[] lines;
        try {

            FileReader file = new FileReader(pathToFile);
            BufferedReader varRead = new BufferedReader(file);
            int num = numStrings(pathToFile);
            lines = new String[num];


            for (int i = 0; i < num; i++) {
                lines[i] = varRead.readLine();
            }


            varRead.close();
            file.close();
        } catch (IOException e) {
            log.error("Can't read file: (" + pathToFile + "). Error: " + e);
            throw new RuntimeException(e);
        }
        return lines;
    }


    public void writeFile(String path, String data) throws IOException {
        FileWriter configFile = new FileWriter(path);
        configFile.write(data);
        configFile.flush();
        configFile.close();
    }


    private int numStrings(String pathToFile) throws IOException {

        FileReader text = new FileReader(pathToFile);
        BufferedReader y = new BufferedReader(text);


        String one;
        int num = 0;


        while ((one = y.readLine()) != null) {
            num++;
        }


        y.close();
        text.close();
        return num;
    }


}