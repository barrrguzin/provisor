package ru.ptkom.provisor.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.ptkom.provisor.exception.EltexHttpTerminalErrorException;
import ru.ptkom.provisor.exception.EltexInvalidCredentialsException;
import ru.ptkom.provisor.models.alias_list2.Out;
import ru.ptkom.provisor.service.ApplicationPropertiesFileService;


import javax.net.ssl.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;


@Slf4j
@Component
public class ECSSAPIClient {

//    @Autowired
//    @Qualifier("RestTemplate")
      private final RestTemplate restTemplate;
      private final ApplicationPropertiesFileService applicationPropertiesFileService;


      private static String URLECSS;
      private static String LoginECSS;
      private static String PasswordECSS;
      private static String DomainECSS;
      private static String GroupECSS;
      private static String usersURL;
      private static String aliasesURL;
      private static String authenticationURL;
      private static String authenticationCheckURL;
      private static String authenticationRequestBody;
      private static String TOKEN = "";


    static {
        disableSslVerification();
    }

    public ECSSAPIClient(@Qualifier("RestTemplate") RestTemplate restTemplate, ApplicationPropertiesFileService applicationPropertiesFileService) {
        this.restTemplate = restTemplate;
        this.applicationPropertiesFileService = applicationPropertiesFileService;
        URLECSS = applicationPropertiesFileService.getURLECSS();
        LoginECSS = applicationPropertiesFileService.getLoginECSS();
        PasswordECSS = applicationPropertiesFileService.getPasswordECSS();
        DomainECSS = applicationPropertiesFileService.getDomainECSS();
        GroupECSS = applicationPropertiesFileService.getGroupECSS();
        usersURL = "https://" + URLECSS + ":9999/commands/sip_user_show";
        aliasesURL = "https://" + URLECSS + ":9999/commands/alias_list2";
        authenticationURL = "https://" + URLECSS + ":9999/system/login";
        authenticationCheckURL = "https://" + URLECSS + ":9999/system/is_active";
        authenticationRequestBody  = "<in><login user = \"" + LoginECSS + "\" password=\"" + PasswordECSS + "\" /></in>";
    }


    public Out getListOfAliases(){

        ru.ptkom.provisor.models.alias_list2.In request = new ru.ptkom.provisor.models.alias_list2.In(DomainECSS, "{100-200}", "100");
        ResponseEntity response = sendRequestAndReAuthenticateIfFirstTryIsUnsuccessful(request.toXML(), aliasesURL);

        return mapXMLAliasesToObject(response.getBody().toString());
    }


    private Out mapXMLAliasesToObject(String aliasesXML) {

        try {
            JAXBContext context = JAXBContext.newInstance(Out.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader xml = new StringReader(aliasesXML);
            Out aliasesList = (Out) unmarshaller.unmarshal(xml);
            return aliasesList;

        } catch (JAXBException e) {
            log.error("Unmarshalling end with error: " + e.getMessage());
            throw new RuntimeException("Unmarshalling end with error: " + e.getMessage());

        }
    }


    private void getAuth(){

        try {
            ResponseEntity response = sendRequest(authenticationRequestBody, authenticationURL);
            String token = response.getHeaders().getFirst("Set-Cookie");
            TOKEN = token;

        } catch (EltexInvalidCredentialsException e) {
            throw new RuntimeException(e);

        } catch (EltexHttpTerminalErrorException e) {
            throw new RuntimeException(e);

        }
    }


    private Integer getCheck(){
        int statusCode = 666;
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", TOKEN);
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        try {
            ResponseEntity response = restTemplate.exchange(authenticationCheckURL, HttpMethod.GET, requestEntity, String.class);
            statusCode = response.getStatusCode().value();
        } finally {
          return statusCode;
        }
    }


    private void reAuthIfNeeded(){
        if(TOKEN == ""){
            getAuth();
        }
        if (getCheck() != 200){
            getAuth();
        }
    }


    public ru.ptkom.provisor.models.sip_user_show.Out getAliasData(String number) {

        ru.ptkom.provisor.models.sip_user_show.In request = new ru.ptkom.provisor.models.sip_user_show.In(number, DomainECSS, GroupECSS, true, true);
        ResponseEntity response = sendRequestAndReAuthenticateIfFirstTryIsUnsuccessful(request.toXML(), usersURL);

        return mapXMLAliasDataToObject(response.getBody().toString());
    }


    private ru.ptkom.provisor.models.sip_user_show.Out mapXMLAliasDataToObject(String aliasXML) {

        try {
            JAXBContext context = JAXBContext.newInstance(ru.ptkom.provisor.models.sip_user_show.Out.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader xml = new StringReader(aliasXML);
            ru.ptkom.provisor.models.sip_user_show.Out aliasesList = (ru.ptkom.provisor.models.sip_user_show.Out) unmarshaller.unmarshal(xml);
            return aliasesList;

        } catch (JAXBException e) {
            log.error("Unmarshalling end with error: " + e.getMessage());
            throw new RuntimeException("Unmarshalling end with error: " + e.getMessage());

        }
    }


    private ResponseEntity sendRequest(String requestBody, String url) throws EltexInvalidCredentialsException, EltexHttpTerminalErrorException {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", TOKEN);
        HttpEntity httpEntity = new HttpEntity(requestBody, headers);

        ResponseEntity response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        HttpStatus statusCode = response.getStatusCode();

        if (statusCode.is2xxSuccessful()) {
            return response;

        } else if (statusCode.is4xxClientError()) {
            throw new EltexInvalidCredentialsException("Credentials is invalid, check it in config file");

        } else {
            throw new EltexHttpTerminalErrorException("ECSS-10 return error with code " + statusCode + ". It says: " + response.getBody());
        }
    }


    private ResponseEntity sendRequestAndReAuthenticateIfFirstTryIsUnsuccessful(String requestBody, String url) {
        try {
            ResponseEntity serverResponse = sendRequest(requestBody, url);
            return serverResponse;

        } catch (Exception e) { //catch (EltexInvalidCredentialsException e) {
            getAuth();

            try {
                ResponseEntity serverResponse = sendRequest(requestBody, url);
                return serverResponse;

            } catch (EltexInvalidCredentialsException ex) {
                throw new RuntimeException(ex);

            } catch (EltexHttpTerminalErrorException ex) {
                throw new RuntimeException(ex);
            }

        }
    }


    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }


}