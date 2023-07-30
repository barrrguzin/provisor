package ru.ptkom.provisor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ApplicationPropertiesFileService {

    @Value("${spa.admin.name}")
    private String adminNameOnSPA9XX;
    @Value("${spa.admin.pass}")
    private String adminPasswordOnSPA9XX;
    @Value("${path.spa921.init}")
    private String pathToSPA921InitializingConfigurationFile;
    @Value("${path.spa941.init}")
    private String pathToSPA941InitializingConfigurationFile;
    @Value("${path.snrvp51.init}")
    private String pathToSNRVP51InitializingConfigurationFile;
    @Value("${path.snrvp52.init}")
    private String pathToSNRVP52InitializingConfigurationFile;
    @Value("${path.spa.templ}")
    private String pathToSPA9XXConfigurationFileTemplate;
    @Value("${path.snrvp.templ}")
    private String pathToSNRVP5XConfigurationFileTemplate;
    @Value("${path.spa.confs}")
    private String pathToGeneratedConfigurationFilesToSPA9XX;
    @Value("${path.snrvp.confs}")
    private String pathToGeneratedConfigurationFilesToSNRVP5X;
    @Value("${dhcp.service.configuration.path}")
    private String pathToDHCPServiceConfigurationFile;
    @Value("${dhcp.service.name}")
    private String DHCPServiceName;
    @Value("${tftp.service.configuration.path}")
    private String pathToTFTPServiceConfigurationFile;
    @Value("${tftp.service.name}")
    private String TFTPServiceName;
    @Value("${host.arp.table.path}")
    private String pathToARPTable;
    @Value("${ecss.url}")
    private String URLECSS;
    @Value("${ecss.login}")
    private String LoginECSS;
    @Value("${ecss.password}")
    private String PasswordECSS;
    @Value("${ecss.domain}")
    private String DomainECSS;
    @Value("${ecss.group}")
    private String GroupECSS;
    @Value("${host.ip.address}")
    private String hostIPAddress;
    @Value("${view.application.url}")
    private String viewApplicationURL;
    @Value("${spa.digest.realm}")
    private String SPADigestRealm;


    public String getPathToSPA921InitializingConfigurationFile() {
        return pathToSPA921InitializingConfigurationFile;
    }

    public String getPathToSPA941InitializingConfigurationFile() {
        return pathToSPA941InitializingConfigurationFile;
    }

    public String getPathToSNRVP51InitializingConfigurationFile() {
        return pathToSNRVP51InitializingConfigurationFile;
    }

    public String getPathToSNRVP52InitializingConfigurationFile() {
        return pathToSNRVP52InitializingConfigurationFile;
    }

    public String getPathToSPA9XXConfigurationFileTemplate() {
        return pathToSPA9XXConfigurationFileTemplate;
    }

    public String getPathToSNRVP5XConfigurationFileTemplate() {
        return pathToSNRVP5XConfigurationFileTemplate;
    }

    public String getPathToGeneratedConfigurationFilesToSPA9XX() {
        return pathToGeneratedConfigurationFilesToSPA9XX;
    }

    public String getPathToGeneratedConfigurationFilesToSNRVP5X() {
        return pathToGeneratedConfigurationFilesToSNRVP5X;
    }

    public String getPathToDHCPServiceConfigurationFile() {
        return pathToDHCPServiceConfigurationFile;
    }

    public String getPathToTFTPServiceConfigurationFile() {
        return pathToTFTPServiceConfigurationFile;
    }

    public String getPathToARPTable() { return pathToARPTable; }

    public String getDHCPServiceName() {
        return DHCPServiceName;
    }

    public String getTFTPServiceName() {
        return TFTPServiceName;
    }

    public String getURLECSS() {
        return URLECSS;
    }

    public String getLoginECSS() {
        return LoginECSS;
    }

    public String getPasswordECSS() {
        return PasswordECSS;
    }

    public String getDomainECSS() {
        return DomainECSS;
    }

    public String getGroupECSS() {
        return GroupECSS;
    }

    public String getHostIPAddress() {
        return hostIPAddress;
    }

    public String getAdminNameOnSPA9XX() {
        return adminNameOnSPA9XX;
    }

    public String getAdminPasswordOnSPA9XX() {
        return adminPasswordOnSPA9XX;
    }

    public String getViewApplicationURL() {
        return viewApplicationURL;
    }

    public String getSPADigestRealm() {
        return SPADigestRealm;
    }
}
