package ru.ptkom.provisor.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.client.ECSSAPIClient;
import ru.ptkom.provisor.models.alias_list2.AliasType;
import ru.ptkom.provisor.models.alias_list2.Out;
import ru.ptkom.provisor.models.aliases.OutAliases;
import ru.ptkom.provisor.models.sipUsers.OutUsers;
import ru.ptkom.provisor.models.sip_user_show.UserType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.lang.invoke.WrongMethodTypeException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Component
public class ECSSUserDataDAO {


    private final ECSSAPIClient ecssapiClient;

    public ECSSUserDataDAO(ECSSAPIClient ecssapiClient) {
        this.ecssapiClient = ecssapiClient;
    }


    public List<AliasType> listOfAliases(){
        Out ecssResponse = ecssapiClient.getListOfAliases();
        List<AliasType> listOfAliases = ecssResponse.getResult().getAlias();
        log.info("From ECSS got " + listOfAliases.size() + " aliases (numbers and names).");
        return listOfAliases;
    }


    public UserType allAliasData(String number) {
        ru.ptkom.provisor.models.sip_user_show.Out esccResponse = ecssapiClient.getAliasData(number);

        List<UserType> listOfUsers = esccResponse.getUsers().getUser();

        if (listOfUsers.size() == 1) {
            UserType user = listOfUsers.get(0);
            log.info("From ECSS got data about SIP account with number: " + user.getIface() + ".");
            return user;

        } else {
            log.error("There is more one user with number " + number + " in list! SIP user must have a unique number!");
            throw new WrongMethodTypeException("There is more one user with number " + number + " in list! SIP user must have a unique number!");
            
        }
    }


    private OutUsers  xmlToUsersObject(String xml) {


        xml = xml.replaceAll("sip &lt;sip", "sip")
                .replaceAll(">;", "; ");


        try {
            StringReader sr =new StringReader(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(OutUsers.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            OutUsers respone = (OutUsers) unmarshaller.unmarshal(sr);
            return respone;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }}


    private OutAliases  xmlToAliasesObject(String xml) {
        try {
            StringReader sr =new StringReader(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(OutAliases.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            OutAliases respone = (OutAliases) unmarshaller.unmarshal(sr);
            return respone;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }}


}