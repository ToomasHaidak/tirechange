package com.example.tirechange.service;

public class WorkshopDTO {

    private int workshopID;
    private String workshopName;
    private String workshopAddress;
    private String[] vehiclesServiced;

    public int getWorkshopID() {
        return workshopID;
    }

    public void setWorkshopID(int workshopID) {
        this.workshopID = workshopID;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getWorkshopAddress() {
        return workshopAddress;
    }

    public void setWorkshopAddress(String workshopAddress) {
        this.workshopAddress = workshopAddress;
    }

    public String[] getVehiclesServiced() {
        return vehiclesServiced;
    }

    public void setVehiclesServiced(String[] vehiclesServiced) {
        this.vehiclesServiced = vehiclesServiced;
    }
}
