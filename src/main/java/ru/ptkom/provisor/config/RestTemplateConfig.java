package ru.ptkom.provisor.config;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import ru.ptkom.provisor.service.ApplicationPropertiesFileService;

@Configuration
public class RestTemplateConfig {


    private final ApplicationPropertiesFileService applicationPropertiesFileService;
    private static String hostIPAddress;
    private static String adminNameSPA9XX;
    private static String adminPasswordSPA9XX;
    private static String realmNameSPA;


    public RestTemplateConfig(ApplicationPropertiesFileService applicationPropertiesFileService) {
        this.applicationPropertiesFileService = applicationPropertiesFileService;
        hostIPAddress = applicationPropertiesFileService.getHostIPAddress();
        adminNameSPA9XX = applicationPropertiesFileService.getAdminNameOnSPA9XX();
        adminPasswordSPA9XX = applicationPropertiesFileService.getAdminPasswordOnSPA9XX();
        realmNameSPA = applicationPropertiesFileService.getSPADigestRealm();
    }


    @Bean(name = "RestTemplate")
    @Primary
    public RestTemplate defaultRestTemplate() {
        return new RestTemplate();
    }


    @Bean(name = "RestTemplateWithDigestAuth")
    public RestTemplate restTemplateWithDigestAuth() {
        HttpHost host = new HttpHost(hostIPAddress, 80, "http");
        CloseableHttpClient client = HttpClientBuilder.create().
                setDefaultCredentialsProvider(provider()).useSystemProperties().build();
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactoryDigestAuth(host, client, realmNameSPA);

        return new RestTemplate(requestFactory);
    }


    private CredentialsProvider provider() {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials =
                new UsernamePasswordCredentials(adminNameSPA9XX, adminPasswordSPA9XX);
        provider.setCredentials(AuthScope.ANY, credentials);
        return provider;
    }


}