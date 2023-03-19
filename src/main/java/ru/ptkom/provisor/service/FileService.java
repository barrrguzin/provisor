package ru.ptkom.provisor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Service
public class FileService {


    public String[] openAndRead(String pathToFile) {


        FileReader file = null;
        try {
            file = new FileReader(pathToFile);
        } catch (FileNotFoundException e) {
            log.error("Can't read file: (" + pathToFile + "). Error: " + e);
            throw new RuntimeException(e);
        }
        BufferedReader varRead = new BufferedReader(file);

        int num = 0;
        try {
            num = numStrings(pathToFile);
        } catch (IOException e) {
            log.error("Can't read file: (" + pathToFile + "). Error: " + e);
            throw new RuntimeException(e);
        }
        String[] lines = new String[num];


        for (int i = 0; i < num; i++) {
            try {
                lines[i] = varRead.readLine();
            } catch (IOException e) {
                log.error("Can't read line from file (" + pathToFile + "). Error: " + e);
                throw new RuntimeException(e);
            }
        }

        try {
            varRead.close();
        } catch (IOException e) {
            log.error("Can't close file (" + pathToFile + "). Error: " + e);
            throw new RuntimeException(e);
        }
        return lines;
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

        return num;
    }

}
