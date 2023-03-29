package ru.ptkom.provisor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.ptkom.provisor.config.PropertiesConfig;
import ru.ptkom.provisor.network.Ping;

import java.io.IOException;
import java.net.UnknownHostException;


@Slf4j
@Service
public class NetworkService {


    @Autowired
    private FileService fileService;



    private Ping ping;
    private int counter;
    private static final String PATH_TO_ARP_TABLE;

    private static final int LAST_ADDRESS_IN_NETWORK = 254;

    static {
        ApplicationContext context = new AnnotationConfigApplicationContext(PropertiesConfig.class);
        Environment config = context.getEnvironment();
        PATH_TO_ARP_TABLE = config.getProperty("arp.table.path");
    }


    public String[][] showActiveHosts(String net) {
        long bradcast = System.currentTimeMillis();
        broadcastPing(net);
        log.debug("Broadcast finished for: " + ((System.currentTimeMillis() - bradcast)/1000f) + " seconds.");


        long scan = System.currentTimeMillis();
        int[] addressLastBytes = scanNetwork(net);
        log.debug("Scanning finished for: " + ((System.currentTimeMillis() - scan)/1000f) + " seconds.");



        String[][] addressAndMacOfActiveHosts = new String[addressLastBytes.length][2];


        long arp = System.currentTimeMillis();
        String[][] arpTableData = showArpTable();
        log.debug("Got data from ARP table finished for: " + ((System.currentTimeMillis() - arp)/1000f) + " seconds.");


        net = net.substring(0, net.lastIndexOf(".")) + ".";


        long parse = System.currentTimeMillis();
        for (int i = 0; i < addressLastBytes.length; i++) {
            String addressString = net+addressLastBytes[i];
            addressAndMacOfActiveHosts[i][0] = addressString;
            for (int j = 0; j<arpTableData.length; j++) {
                if (arpTableData[j][0] == null) {
                    break;
                }
                if (addressString.equals(arpTableData[j][0])) {
                    addressAndMacOfActiveHosts[i][1] = arpTableData[j][1].replaceAll(":","");
                    break;
                }
            }
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


    public void broadcastPing(String net) {

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


    public String[][] showArpTable(){

        String[] arpTableLines = getArpTable();
        String[][] ipAndMacAddresses = new String[arpTableLines.length][2];

        for (int i = 0; i<arpTableLines.length; i++){
            ipAndMacAddresses[i][0] = arpTableLines[i].split("\\s+")[0];
            ipAndMacAddresses[i][1] = arpTableLines[i].split("\\s+")[3];
        }
        return ipAndMacAddresses;
    }

    private String[] getArpTable() {
        String[] arpTableLines = fileService.openAndRead(PATH_TO_ARP_TABLE);
        return arpTableLines;
    }
}