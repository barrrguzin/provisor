package ru.ptkom.provisor.network;


import java.io.IOException;
import java.net.*;


public class UdpSocket extends Thread {
    private DatagramSocket socket;
    private byte[] buf = new byte[1024];

    public UdpSocket(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }



    public void run() {

        try {

            while (true) {


                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String data = new String(packet.getData()).trim();
                System.out.println(data);

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}



//public class SipSocket extends Thread {
//    private ServerSocket serverSocket;
//    private Socket clientSocket;
//    private PrintWriter out;
//    private BufferedReader in;
//    private int port;
//
//    public SipSocket(int port) {
//        this.port = port;
//        System.out.println("PortSet");
//    }
//
//
//    public void run() {
//        try {
//            System.out.println("StartLower");
//            serverSocket = new ServerSocket(port);
//            System.out.println("ServSockLower");
//            clientSocket = serverSocket.accept();
//            System.out.println("ClientSockLower");
//            System.out.println("Cocket start!");
//
//
//            out = new PrintWriter(clientSocket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            String greeting = in.readLine();
//            System.out.println(greeting);
//
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}



