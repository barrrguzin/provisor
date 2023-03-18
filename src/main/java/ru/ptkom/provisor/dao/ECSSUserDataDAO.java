package ru.ptkom.provisor.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ptkom.provisor.client.ECSSAPIClient;
import ru.ptkom.provisor.models.aliases.OutAliases;
import ru.ptkom.provisor.models.sipUsers.OutUsers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Component
public class ECSSUserDataDAO {


    @Autowired
    private ECSSAPIClient ecssapiClient;


    public List<List<String>> listOfAliasesNames(){


        String xmlListOfAliases = ecssapiClient.getListOfAliases();


        OutAliases data = xmlToAliasesObject(xmlListOfAliases);
        List<List<String>> numbersAndNames = new ArrayList<>();

        for (int index=0; index<data.getAliases().size();index++){
            numbersAndNames.add(new ArrayList<String>(Arrays.asList(data.getAliases().get(index).getAddress(), data.getAliases().get(index).getDisplay_name())));
        }
        return numbersAndNames;
    }


    public OutUsers.User allAliasData(String number) {
        String xml = ecssapiClient.getAliasData(number);
        OutUsers data = xmlToUsersObject(xml);
        return data.getUsers().get(0);
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