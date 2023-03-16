package ru.ptkom.provisor.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class FileService {


    public String[] openAndRead(String pathToFile) throws IOException {


        FileReader file = new FileReader(pathToFile);
        BufferedReader varRead = new BufferedReader(file);

        int num = numStrings(pathToFile);
        String[] lines = new String[num];


        for (int i = 0; i < num; i++) {
            lines[i] = varRead.readLine();
        }

        varRead.close();
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
