package ru.ptkom.provisor.client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.ptkom.provisor.config.PropertiesConfig;
import ru.ptkom.provisor.models.sipUsers.In;


import javax.net.ssl.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ECSSAPIClient {


    private static final String ECSS_IP_ADDRESS;
    private static final String LOGIN;
    private static final String PASSWORD;
    private static final String DOMAIN;
    private static final String GROUP;


    private static final String GET_USERS_URL;
    private static final String GET_ALIASES_URL;
    private static final String GET_AUTH_URL;
    private static final String GET_AUTH_CHECK_URL;
    private static final String AUTH_REQUEST;


    private static final String LIST_OF_ALIASES_REQUEST = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<in\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"hc_alias_list2.xsd\">\n" +
            "<request address_range=\"{100-200}\" domain=\"voip.ptk.loc\" limit=\"100\">\n" +
            "<filters>\n" +
            "</filters>\n" +
            "</request>\n" +
            "</in>";


    private static String TOKEN = "";


    static {
        //works
        ApplicationContext context = new AnnotationConfigApplicationContext(PropertiesConfig.class);
        Environment config = context.getEnvironment();


        ECSS_IP_ADDRESS = config.getProperty("ecss.url");
        LOGIN = config.getProperty("ecss.login");
        PASSWORD = config.getProperty("ecss.password");
        DOMAIN = config.getProperty("ecss.domain");
        GROUP = config.getProperty("ecss.group");


        GET_USERS_URL = "https://" + ECSS_IP_ADDRESS + ":9999/commands/sip_user_show";
        GET_ALIASES_URL = "https://" + ECSS_IP_ADDRESS + ":9999/commands/alias_list2";
        GET_AUTH_URL = "https://" + ECSS_IP_ADDRESS + ":9999/system/login";
        GET_AUTH_CHECK_URL = "https://" + ECSS_IP_ADDRESS + ":9999/system/is_active";
        AUTH_REQUEST = "<in><login user = \"" + LOGIN + "\" password=\"" + PASSWORD + "\" /></in>";


        disableSslVerification();
    }



    public String getListOfAliases(){
        reAutfIfNeeded();


        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", TOKEN);
        HttpEntity requestEntity = new HttpEntity(LIST_OF_ALIASES_REQUEST,requestHeaders);
        ResponseEntity response = restTemplate.exchange(GET_ALIASES_URL, HttpMethod.POST, requestEntity, String.class);

        return response.getBody().toString();
    }


    private void getAuth(){

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        //requestHeaders.add("Cookie", "token="+TOKEN);
        HttpEntity requestEntity = new HttpEntity(AUTH_REQUEST, requestHeaders);
        ResponseEntity response = restTemplate.exchange(GET_AUTH_URL, HttpMethod.POST, requestEntity, String.class);
        String token = response.getHeaders().getFirst("Set-Cookie");
        TOKEN = token;
    }


    private Integer getCheck(){
        int statusCode = 666;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", TOKEN);
        HttpEntity requestEntity = new HttpEntity(requestHeaders);
        try {
            ResponseEntity response = restTemplate.exchange(GET_AUTH_CHECK_URL, HttpMethod.GET, requestEntity, String.class);
            statusCode = response.getStatusCode().value();
        } finally {
          return statusCode;
        }
    }


    private void reAutfIfNeeded(){
        if(TOKEN == ""){
            getAuth();
        }
        if (getCheck() != 200){
            getAuth();
        }
    }


    public String getAliases(){


        reAutfIfNeeded();
        In request = new In();
        In.Sip sip = new In.Sip();
        List<In.Sip> listOfSip= new ArrayList<>();
        sip.setDomain(DOMAIN);
        sip.setGroup(GROUP);
        sip.setComplete(false);
        sip.setAuth(false);
        listOfSip.add(sip);
        request.setSip(listOfSip);


        return sendRequest(request).getBody().toString();
    }


public String getAliasData(String number) {


        reAutfIfNeeded();
        In request = new In();
        In.Sip sip = new In.Sip();
        List<In.Sip> listOfSip = new ArrayList<>();


        if (number.contains("@")) {
            sip.setId(number.toString());
        } else {
            sip.setId(number + "@" + DOMAIN);
        }


        sip.setDomain(DOMAIN);
        sip.setGroup(GROUP);
        sip.setComplete(true);
        sip.setAuth(true);
        listOfSip.add(sip);
        request.setSip(listOfSip);

        //System.out.println(sendRequest(request).getBody().toString());
        return sendRequest(request).getBody().toString();
}




private ResponseEntity sendRequest(In request){
    JAXBContext jaxbContext = null;
    try {
        jaxbContext = JAXBContext.newInstance(In.class);
    } catch (JAXBException e) {
        throw new RuntimeException(e);
    }
    Marshaller jaxbMarshaller = null;
    try {
        jaxbMarshaller = jaxbContext.createMarshaller();
    } catch (JAXBException e) {
        throw new RuntimeException(e);
    }
    //jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
    StringWriter sw = new StringWriter();
    try {
        jaxbMarshaller.marshal(request, sw);
    } catch (JAXBException e) {
        throw new RuntimeException(e);
    }
    String xmlContent = sw.toString();



    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add("Cookie", TOKEN);
    HttpEntity requestEntity = new HttpEntity(xmlContent, requestHeaders);
    ResponseEntity response = restTemplate.exchange(GET_USERS_URL, HttpMethod.POST, requestEntity, String.class);
    return response;
}















    private static void disableSslVerification() {
        try
        {
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



//    public void getAliases(){
//
//
//        In request = new In();
//        Sip sip = new Sip();
//        List<Sip> listOfSip= new ArrayList<>();
//        sip.setDomain("voip.ptk.loc");
//        sip.setGroup("*");
//        sip.setComplete(false);
//        listOfSip.add(sip);
//        request.setSip(listOfSip);
//
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders requestHeaders = new HttpHeaders();
//        requestHeaders.add("Cookie", TOKEN);
//        HttpEntity requestEntity = new HttpEntity(request, requestHeaders);
//        ResponseEntity response = restTemplate.exchange(GET_ALL_USERS_URL, HttpMethod.POST, requestEntity, String.class);
//
//
//        String xml = (String) response.getBody();
//        System.out.println(xml);
//
//    }
