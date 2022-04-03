package com.example.tirechange.controller;

import com.example.tirechange.AvailableTimesDTO;
import com.example.tirechange.service.TirechangeService;
import com.example.tirechange.service.WorkshopDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TirechangeController {

    @Autowired
    private TirechangeService tirechangeService;

    @GetMapping("api/backend/getWorkshopList")
    public List<String> getWorkshopList() {
        return tirechangeService.getWorkshopList();
    }

    @GetMapping("api/backend/getVehicleTypeList")
    public List<String> getVehicleTypeList() {
        return tirechangeService.getVehicleTypeList();
    }

    @PostMapping("api/backend/addNewWorkshop")
    public String addNewWorkshop(@RequestBody WorkshopDTO workshopData) {
        tirechangeService.addNewWorkshop(workshopData);
        return "true";
    }

    @PostMapping("api/backend/updateWorkshopData/{workshop}")
    public void updateWorkshopData(@PathVariable("workshop") String workshopName, @RequestBody WorkshopDTO workshopData) {
        tirechangeService.updateWorkshopData(workshopName, workshopData);
    }

    @GetMapping("api/backend/getWorkshopData/{workshop}")
    public List<String> getWorkshopData(@PathVariable("workshop") String workshopName) {
        return tirechangeService.getWorkshopData(workshopName);
    }

    @GetMapping("api/backend/getAvailableTimes/{workshopName}/{vehicleTypeName}/{from}/{until}")
    public List<AvailableTimesDTO> getAvailableTimes(@PathVariable("workshopName") String workshopName,
                                                     @PathVariable("vehicleTypeName") String vehicleTypeName,
                                                     @PathVariable("from") String from,
                                                     @PathVariable("until") String until) {
        return tirechangeService.getAvailableTimesFromWorkshops(workshopName, vehicleTypeName, from, until);
    }

    @PostMapping("api/backend/bookTime")
    public String bookTime(@RequestBody AvailableTimesDTO bookTime) {
        return tirechangeService.bookTime(bookTime.getWorkshopName(), bookTime.getId());
    }

}
