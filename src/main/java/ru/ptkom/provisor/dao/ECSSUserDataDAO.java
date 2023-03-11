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


    public List<List<String>> listOfAliasesNamesTest(String xml){
         OutAliases data = xmlToAliasesObject(xml);
        List<List<String>> ifacesAndNames = new ArrayList<>();

        for (int index=0; index<data.getAliases().size();index++){
            ifacesAndNames.add(new ArrayList<String>(Arrays.asList(data.getAliases().get(index).getAddress(), data.getAliases().get(index).getDisplay_name())));
        }
        return ifacesAndNames;
    }


    public List<List<String>> listOfAliases(String xml){
        OutAliases data = xmlToAliasesObject(xml);
        List<List<String>> ifacesAndNames = new ArrayList<>();
        for (int index=0; index<data.getAliases().size();index++) {
            String number = data.getAliases().get(index).getAddress();
            String name = data.getAliases().get(index).getDisplay_name();


            ifacesAndNames.add(new ArrayList<String>(Arrays.asList(number, name)));
        }
        return ifacesAndNames;
    }


    public List<String> listOfAliasesNames(String xml){
        OutUsers data = xmlToUsersObject(xml);
        List<String> ifaces = new ArrayList<>();
        for (int index=0; index<data.getUsers().size();index++){
            ifaces.add(data.getUsers().get(index).getIface());
        }
        return ifaces;
    }


    public OutUsers.User allAliasData(String number) {
        String xml = ecssapiClient.getAliasData(number);
        OutUsers data = xmlToUsersObject(xml);
        System.out.println(xml);
        System.out.println(data.getUsers().get(0).getLast_via());
        return data.getUsers().get(0);
    }









    private OutUsers  xmlToUsersObject(String xml) {


        //sip &lt;sip
        //sip <sip
        xml = xml.replaceAll("sip &lt;sip", "sip")
                .replaceAll(">;", "; ");


        try {
            StringReader sr =new StringReader(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(OutUsers.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            OutUsers respone = (OutUsers) unmarshaller.unmarshal(sr);
            System.out.println(respone.getUsers().get(0).getContact());
            return respone;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }}


    private OutAliases  xmlToAliasesObject(String xml) {


//        xml = xml.replaceAll("<sip", "&lt;sip");
//        xml = xml.replaceAll(">;", "&gt;;");



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

