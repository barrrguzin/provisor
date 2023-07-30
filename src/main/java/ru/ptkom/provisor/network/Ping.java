package ru.ptkom.provisor.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Ping extends Thread{

    private InetAddress inetAddress;
    private volatile boolean reachable;

    public Ping(String address) throws UnknownHostException {
        this.inetAddress = InetAddress.getByName(address);
    }

    public void run() {


        reachable = false;
        //long start = System.currentTimeMillis();

        try {


            reachable = inetAddress.isReachable(25);
            if (reachable != true){
                reachable = inetAddress.isReachable(250);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean getResult() {
        return reachable;
    }

}
