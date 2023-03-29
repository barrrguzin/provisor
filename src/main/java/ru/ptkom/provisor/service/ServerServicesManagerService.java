package ru.ptkom.provisor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ptkom.provisor.config.PropertiesConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class ServerServicesManagerService {


    private static final String DHCP_SERVICE_NAME;
    private static final String TFTP_SERVICE_NAME;


    static {
        ApplicationContext context = new AnnotationConfigApplicationContext(PropertiesConfig.class);
        Environment config = context.getEnvironment();
        DHCP_SERVICE_NAME = config.getProperty("dhcp.service.name");
        TFTP_SERVICE_NAME = config.getProperty("tftp.service.name");
    }


    public String getDHCPServiceStatus() {
        return getServiceStatus(DHCP_SERVICE_NAME);
    }


    public String getTFTPServiceStatus() {
        return getServiceStatus(TFTP_SERVICE_NAME);
    }


    public String restartDHCPService() {
        return restartService(DHCP_SERVICE_NAME);
    }


    public String restartTFTPService() {
        return restartService(TFTP_SERVICE_NAME);
    }


    public String startDHCPService() {
        return startService(DHCP_SERVICE_NAME);
    }


    public String startTFTPService() {
        return startService(TFTP_SERVICE_NAME);
    }


    public String stopDHCPService() {
        return stopService(DHCP_SERVICE_NAME);
    }


    public String stopTFTPService() {
        return stopService(TFTP_SERVICE_NAME);
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

