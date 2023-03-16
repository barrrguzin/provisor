package ru.ptkom.provisor.service;


import org.springframework.stereotype.Service;
import ru.ptkom.provisor.network.UdpSocket;

import java.net.SocketException;

@Service
public class UdpSocketService {

    private UdpSocket udpSocket;


    public void startUdpSocket(int port) {
        try {
            udpSocket = new UdpSocket(port);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        udpSocket.start();
    }


    public void stopUdpSocket() {
        if (udpSocket != null) {
            udpSocket.interrupt();
            udpSocket = null;
        }
    }

}
