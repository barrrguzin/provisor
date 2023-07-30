package ru.ptkom.provisor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ptkom.provisor.dao.PBXUserDAO;
import ru.ptkom.provisor.models.PBXUser;
import ru.ptkom.provisor.models.networkScan.ActiveHost;
import ru.ptkom.provisor.models.networkScan.FoundAddressData;
import ru.ptkom.provisor.network.Ping;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class NetworkService {



    private final FileService fileService;
    private final PBXUserDAO pbxUserDAO;
    private final ApplicationPropertiesFileService applicationPropertiesFileService;


    private int counter;
    private static String pathToARPTable;
    private static final int LAST_ADDRESS_IN_NETWORK = 254;

    @Autowired
    public NetworkService(FileService fileService, PBXUserDAO pbxUserDAO, ApplicationPropertiesFileService applicationPropertiesFileService) {
        this.fileService = fileService;
        this.pbxUserDAO = pbxUserDAO;
        this.applicationPropertiesFileService = applicationPropertiesFileService;
        pathToARPTable = applicationPropertiesFileService.getPathToARPTable();
    }


    public List<FoundAddressData> scanNetworkAndSetResult(String serverAddress) {
        long scanStartTime = System.currentTimeMillis();
        List<ActiveHost> ipAndMacAddresses = showActiveHosts(serverAddress);
        int quantityOfAddresses = ipAndMacAddresses.size();
        log.info("Network scanning is done for " + ((System.currentTimeMillis()-scanStartTime)/1000f) + " seconds. "
                + ipAndMacAddresses.size() + " hosts found in network: " + serverAddress + ".");
        List<PBXUser> userData = new ArrayList<>(quantityOfAddresses);

        for (ActiveHost foundAddress : ipAndMacAddresses) {
            String mac = foundAddress.getMacAddress();
            PBXUser phoneUser = pbxUserDAO.findByMac(mac);
            userData.add(phoneUser);
        }
        if (ipAndMacAddresses.size() != 0) {
            List<FoundAddressData> result = new ArrayList<>(quantityOfAddresses);
            for (int i = 0; i < quantityOfAddresses; i++) {
                FoundAddressData addressData = new FoundAddressData(
                        ipAndMacAddresses.get(i).getIpAddress(),
                        ipAndMacAddresses.get(i).getMacAddress(),
                        userData.get(i).getName(),
                        userData.get(i).getNumber(),
                        userData.get(i).getPhoneModel());
                result.add(addressData);
            }
            log.info("Compare user and found MAC addresses. Return page with this.");
            return result;
        } else {
            FoundAddressData noOneAddressFound = new FoundAddressData("IP адресов в сети не обнаружено",
                    "IP адресов в сети не обнаружено",
                    "IP адресов в сети не обнаружено",
                    "IP адресов в сети не обнаружено",
                    "IP адресов в сети не обнаружено");
            List<FoundAddressData> result = new ArrayList<>(1);
            result.add(noOneAddressFound);
            log.info("No one host spot. Return page with this.");
            return result;
        }
    }


    private List<ActiveHost> showActiveHosts(String net) {
        long broadcast = System.currentTimeMillis();
        broadcastPing(net);
        log.debug("Broadcast finished for: " + ((System.currentTimeMillis() - broadcast)/1000f) + " seconds.");


        long scan = System.currentTimeMillis();
        int[] addressLastBytes = scanNetwork(net);
        log.debug("Scanning finished for: " + ((System.currentTimeMillis() - scan)/1000f) + " seconds. Found " + addressLastBytes.length + " addresses");


        List<ActiveHost> addressAndMacOfActiveHosts = new ArrayList<>(addressLastBytes.length);


        long arp = System.currentTimeMillis();
        String[][] arpTableData = showArpTable();
        log.debug("Got data from ARP table finished for: " + ((System.currentTimeMillis() - arp)/1000f) + " seconds.");


        net = net.substring(0, net.lastIndexOf(".")) + ".";

        long parse = System.currentTimeMillis();

        for (int i = 0; i < addressLastBytes.length; i++) {
            String addressString = net+addressLastBytes[i];
            for (int j = 0; j<arpTableData.length; j++) {
                if (arpTableData[j][0] == null) {
                    ActiveHost host = new ActiveHost(addressString, "");
                    addressAndMacOfActiveHosts.add(host);
                    break;
                }
                if (addressString.equals(arpTableData[j][0])) {
                    String mac = arpTableData[j][1].replaceAll(":","");
                    ActiveHost host = new ActiveHost(addressString, mac);
                    addressAndMacOfActiveHosts.add(host);
                    break;
                }
            }
        }

        for (ActiveHost address : addressAndMacOfActiveHosts) {
            log.debug(address.getIpAddress());
        }

        log.debug("Comparing results with ARP table finished for: " + ((System.currentTimeMillis() - parse)/1000f) + " seconds.");
        return addressAndMacOfActiveHosts;
    }


    private int[] scanNetwork(String net) {

        int[] activeAddresses = new int[LAST_ADDRESS_IN_NETWORK];
        counter = 0;

        net = net.substring(0, net.lastIndexOf(".")) + ".";


        Ping[] threads = setUpThreads(LAST_ADDRESS_IN_NETWORK);


        for (int i = 1; i<(LAST_ADDRESS_IN_NETWORK+1); i++){
            String address = net + i;

            try {
                threads[i] = new Ping(address);
                threads[i].start();
            } catch (UnknownHostException e) {
                log.error("Error of setup thread: " + e);
                throw new RuntimeException(e);
            }
        }

        for (int i = 1; i<(LAST_ADDRESS_IN_NETWORK+1); i++){
            try {
                threads[i].join();
                boolean reachable = threads[i].getResult();
                if (reachable == true) {
                    activeAddresses[counter] = i;
                    counter++;
                }
            } catch (InterruptedException e) {
                log.error("Error of got result from thread: " + e);
                throw new RuntimeException(e);
            }
        }


        int[] result = new int[counter];
        for (int i = 0; i < result.length; i++) {
            result[i] = activeAddresses[i];
        }
        counter = 0;
        return result;
    }


    private void broadcastPing(String net) {

        net = net.substring(0, net.lastIndexOf(".")) + ".";


        Ping[] threads = setUpThreads(LAST_ADDRESS_IN_NETWORK);


        for (int i = 1; i<(LAST_ADDRESS_IN_NETWORK+1); i++){
            String address = net + i;
            try {
                threads[i] = new Ping(address);
                threads[i].start();
            } catch (UnknownHostException e) {
                log.error("Error of setup thread: " + e);
                throw new RuntimeException(e);}}


        for (int i = 1; i<(LAST_ADDRESS_IN_NETWORK+1); i++){
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                log.error("Error of waiting to thread stop: " + e);
                throw new RuntimeException(e);}}
    }


    private Ping[] setUpThreads(int lastThreadIndex) {
        return new Ping[lastThreadIndex+1];
    }


    private String[][] showArpTable(){

        String[] arpTableLines = getArpTable();
        String[][] ipAndMacAddresses = new String[arpTableLines.length][2];

        for (int i = 0; i<arpTableLines.length; i++){
            ipAndMacAddresses[i][0] = arpTableLines[i].split("\\s+")[0];
            ipAndMacAddresses[i][1] = arpTableLines[i].split("\\s+")[3];
        }
        return ipAndMacAddresses;
    }

    private String[] getArpTable() {
        String[] arpTableLines = fileService.openAndRead(pathToARPTable);
        return arpTableLines;
    }
}