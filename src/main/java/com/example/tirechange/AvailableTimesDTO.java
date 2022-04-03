package com.example.tirechange;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class AvailableTimesDTO implements Comparable<AvailableTimesDTO> {

    String id;
    String workshopName;
    String workshopAdress;
    String time;
    List<String> vehicleTypeServiced;
    String available;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkshopName() {
        return workshopName;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public String getWorkshopAdress() {
        return workshopAdress;
    }

    public void setWorkshopAdress(String workshopAdress) {
        this.workshopAdress = workshopAdress;
    }

    public List<String> getVehicleTypeServiced() {
        return vehicleTypeServiced;
    }

    public void setVehicleTypeServiced(List<String> vehicleTypeServiced) {
        this.vehicleTypeServiced = vehicleTypeServiced;
    }

    @Override
    public int compareTo(AvailableTimesDTO a) {
        return this.getTime().compareTo(a.getTime());
    }
}
