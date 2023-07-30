package ru.ptkom.provisor.models.networkScan;

public class FoundAddressData {

    private String ipAddress;
    private String macAddress;
    private String name;
    private String phoneNumber;
    private String phoneModel;


    public FoundAddressData(String ipAddress, String macAddress, String name, String phoneNumber, String phoneModel) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.phoneModel = phoneModel;
    }

    public FoundAddressData() {}

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }
}
