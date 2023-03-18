package ru.ptkom.provisor.service;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import java.util.Enumeration;

public class ARP {
    public static void getIpByMac() throws UnknownHostException, SocketException {
        String macAddress = "80:e0:1d:47:3e:82";
        //String macAddress = "AA:BB:CC:DD:EE:FF"; // replace with the MAC address of the device you want to find
        byte[] macBytes = getMacBytes(macAddress.trim());
        InetAddress ipAddress = getIpAddressByMac(macBytes);
        System.out.println(ipAddress.getHostAddress());
    }

    private static byte[] getMacBytes(String macAddress) throws IllegalArgumentException {
        String[] parts = macAddress.split(":");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid MAC address format");
        }
        byte[] macBytes = new byte[6];
        for (int i = 0; i < 6; i++) {
            Integer hex = Integer.parseInt(parts[i], 16);
            macBytes[i] = hex.byteValue();
        }
        return macBytes;
    }

    private static InetAddress getIpAddressByMac(byte[] macBytes) throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            byte[] interfaceMacBytes = networkInterface.getHardwareAddress();
            if (interfaceMacBytes == null) {
                continue;
            }
            if (interfaceMacBytes.length != 6) {
                continue;
            }
            if (java.util.Arrays.equals(macBytes, interfaceMacBytes)) {
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLinkLocalAddress() && !address.isLoopbackAddress() && address.getAddress().length == 4) {
                        return address;
                    }
                }
            }
        }
        throw new SocketException("Could not find IP address for MAC address " + macBytes);
    }
}
