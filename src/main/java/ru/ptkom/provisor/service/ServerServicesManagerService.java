package ru.ptkom.provisor.service;

import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class ServerServicesManagerService {


    private final ApplicationPropertiesFileService applicationPropertiesFileService;


    private static String DHCPServiceName;
    private static String TFTPServiceName;


    public ServerServicesManagerService(ApplicationPropertiesFileService applicationPropertiesFileService) {
        this.applicationPropertiesFileService = applicationPropertiesFileService;
        DHCPServiceName = applicationPropertiesFileService.getDHCPServiceName();
        TFTPServiceName = applicationPropertiesFileService.getTFTPServiceName();
    }



    public String getDHCPServiceStatus() {
        return getServiceStatus(DHCPServiceName);
    }


    public String getTFTPServiceStatus() {
        return getServiceStatus(TFTPServiceName);
    }


    public String restartDHCPService() {
        return restartService(DHCPServiceName);
    }


    public String restartTFTPService() {
        return restartService(TFTPServiceName);
    }


    public String startDHCPService() {
        return startService(DHCPServiceName);
    }


    public String startTFTPService() {
        return startService(TFTPServiceName);
    }


    public String stopDHCPService() {
        return stopService(DHCPServiceName);
    }


    public String stopTFTPService() {
        return stopService(TFTPServiceName);
    }


    private String restartService(String serviceName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("systemctl", "restart", serviceName);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();


            if (exitCode == 0) {
                return serviceName + " restart clear with exit code " + exitCode;
            } else {
                return "Exited with error code : " + exitCode;
            }


        } catch (Exception e) {
            return "Unable to restart " + serviceName;
        }
    }


    private String startService(String serviceName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("systemctl", "start", serviceName);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();


            if (exitCode == 0) {
                return serviceName + " start clear with exit code " + exitCode;
            } else {
                return "Exited with error code : " + exitCode;
            }


        } catch (Exception e) {
            return "Unable to start " + serviceName;
        }
    }


    private String stopService(String serviceName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("systemctl", "stop", serviceName);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();


            if (exitCode == 0) {
                return serviceName + " stop clear with exit code " + exitCode;
            } else {
                return "Exited with error code : " + exitCode;
            }


        } catch (Exception e) {
            return "Unable to stop " + serviceName;
        }
    }


    private String getServiceStatus(String serviceName) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("systemctl", "status", serviceName);
            Process process = processBuilder.start();

            String result = new String();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }

            int exitCode = process.waitFor();
            result += "\nExited with error code : " + exitCode + "\n\n";
            return result;

        } catch (Exception e) {
            return "Unable to get " + serviceName + " status.";
        }
    }
}

