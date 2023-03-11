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
        return data.getUsers().get(0);
    }









    private OutUsers  xmlToUsersObject(String xml) {


        xml = xml.replaceAll("<sip", "&lt;sip").replaceAll(">;", "&gt;;");


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


        xml = xml.replaceAll("<sip", "&lt;sip").replaceAll(">;", "&gt;;");


        try {
            StringReader sr =new StringReader(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(OutAliases.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            OutAliases respone = (OutAliases) unmarshaller.unmarshal(sr);
            return respone;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }}




//    public void makingXml() throws JAXBException {
//
//        String xml = "<?xml version=\"1.0\"?>\n" +
//                "<out\n" +
//                "    xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\" xs:noNamespaceSchemaLocation=\"sip_user_show.xsd\">\n" +
//                "    <users>\n" +
//                "        <user active=\"true\" alarm_enable=\"false\" aliases_list=\"240501\" auth=\"ds\" auth_qop=\"true\" auto_answer_version=\"default\" category_to_sip=\"default\" contact=\"q: 1.0; ip-set: ipset1; on port: 5060; sip <sip:240501@192.168.2.26:5060;transport=UDP;line=42098>;expires=328\" digest=\"true\" display_encoding=\"default\" domain=\"biysk.local\" dtmf_relay=\"false\" fork_mode=\"all-contacts\" group=\"loc.gr\" id=\"06301521b463510a\" iface=\"240501@biysk.local\" last_via=\"SIP/2.0/UDP 192.168.2.26 branch=z9hG4bKPjzAaAU5198fMYk7vFLjbjDNk4AP.GBS6T, received=192.168.2.26, rport=5060;SIP/2.0/UDP 10.22.128.19 received=10.22.128.19, branch=z9hG4bK53e6dedd\" login=\"240501\" my_from=\"biysk.local\" nat_traversal=\"false\" original_cdpn_to=\"true\" owner=\"sip1\" password=\"********\" referred_by_as_cgpn=\"false\" reg_expire_max=\"3600\" reg_expire_min=\"90\" registered_domain_to_invite=\"true\" remote_ctr_indication=\"rpi\" req100rel=\"false\" rfc4028_control=\"force\" routing_context=\"ctx_from_local\" sip_domain=\"biysk.local\" sip_modifications=\"\" symbol_hash_as_is=\"false\" tel_uri_in_diversion=\"false\" transit=\"\" trusted_ip=\"false\" user_agent=\"user-agent: Asterisk PBX 13.1.0~dfsg-1.1ubuntu4.1\"/>\n" +
//                "    </users>\n" +
//                "</out>";
//
//        In dataIn = new In();
//        Sip dataSip1 = new Sip();
//        List<Sip> listOfSip= new ArrayList<>();
//        dataSip1.setDomain("voip.ptk.loc");
//        dataSip1.setGroup("*");
//        dataSip1.setComplete(true);
//        listOfSip.add(dataSip1);
//        dataIn.setSip(listOfSip);
//
//        JAXBContext jaxbContext = JAXBContext.newInstance(In.class);
//        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//        //jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,Boolean.TRUE);
//        StringWriter sw = new StringWriter();
//        jaxbMarshaller.marshal(dataIn, sw);
//        String xmlContent = sw.toString();
//        System.out.println(xmlContent);
//    }


}

