package com.example.tirechange.service;

import com.example.tirechange.AvailableTimesDTO;
import com.example.tirechange.repository.TireChangeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class TirechangeService {


    @Autowired
    TireChangeRepository tireChangeRepository;

    RestTemplate restTemplate = new RestTemplate();
    List<AvailableTimesDTO> timesManchester;
    List<AvailableTimesDTO> timesLondon;
    List<AvailableTimesDTO> allAvailableTimes = new ArrayList<>();


    public List<String> getWorkshopList () {
        return tireChangeRepository.getWorkshopList();
    };

    public List<String> getWorkshopData (String workshopName) {
        return tireChangeRepository.getWorkshopData(workshopName);
    }
    public List<String> getVehicleTypeList () {
        return tireChangeRepository.getVehicleTypeList();
    };

    public List<AvailableTimesDTO> getAvailableTimesFromWorkshops(String workshopName, String vehicleTypeName, String from, String until) {
        allAvailableTimes.clear();
        int workshopID;
        if(workshopName.equals("Kõik töökojad")) {
            workshopID = 0;
        } else {
            workshopID = tireChangeRepository.getWorkshopID(workshopName);
        }
        switch (workshopID) {
                case 0:
                    if(vehicleTypeName.equals("Kõik sõiduki tüübid") || vehicleTypeName.equals("Sõiduauto")) {
                        getTimesFromAPI1(from, until);
                        getTimesFromAPI2(from, until);
                    }
                    if(vehicleTypeName.equals("Veoauto")) {
                        getTimesFromAPI2(from, until);
                    }
                    break;
                case 1:
                    if(vehicleTypeName.equals("Kõik sõiduki tüübid") || vehicleTypeName.equals("Sõiduauto")) {
                        getTimesFromAPI1(from, until);
                    }
                    break;
                case 2:
                    getTimesFromAPI2(from, until);
                    break;
        }
        Collections.sort(allAvailableTimes);
        return allAvailableTimes;
    }

    public void addNewWorkshop(WorkshopDTO workshopData) {
        tireChangeRepository.addNewWorkshopNameAddress(workshopData);
        int workshopID = tireChangeRepository.getWorkshopID(workshopData.getWorkshopName());
    }

    public void updateWorkshopData(String workshopName, WorkshopDTO workshopData) {
        int workshopID = tireChangeRepository.getWorkshopID(workshopName);
        String updatedWorkshopName = workshopData.getWorkshopName();
        String updatedWorkshopAddress = workshopData.getWorkshopAddress();
        tireChangeRepository.updateWorkshopNameAddress(workshopID, updatedWorkshopName, updatedWorkshopAddress);
    }

    public void getTimesFromAPI1(String from, String until) {
        String url = "http://localhost:9003/api/v1/tire-change-times/available?from=" + from + "&until=" + until;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String fullResponse = response.toString();
        String correctedResponse = fullResponse.replace("<200,", "");
        String finalResponse = convertXMLtoJSON(correctedResponse);

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<AvailableTimesDTO>> typeReference = new TypeReference<List<AvailableTimesDTO>>() {};
        try {
            timesLondon = mapper.readValue(finalResponse, typeReference);
            for(AvailableTimesDTO a :timesLondon) {
                a.setAvailable("true");
                setVehicleTypes(a, 1);
                setWorkshopName(a, 1);
                setWorkshopAddress(a, 1);
            }
            allAvailableTimes.addAll(timesLondon);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void getTimesFromAPI2(String from, String until) {
        String url = "http://localhost:9004/api/v2/tire-change-times?from=" + from;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String fullResponse = response.toString();
        String finalResponse = fullResponse.replace("<200,", "");

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<AvailableTimesDTO>> typeReference = new TypeReference<List<AvailableTimesDTO>>() {};
        try {
            timesManchester = mapper.readValue(finalResponse, typeReference);
            for(AvailableTimesDTO a :timesManchester) {
                setVehicleTypes(a, 2);
                setWorkshopName(a, 2);
                setWorkshopAddress(a, 2);
                if(a.getAvailable().equals("true") && a.getTime().substring(0, 10).compareTo(until) <= 0) {
                    allAvailableTimes.add(a);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public String convertXMLtoJSON(String response) {
        try {
            JSONObject json = XML.toJSONObject(response);
            String jsonString = json.toString(4);
            String removeBeginning = jsonString.replace("{\"tireChangeTimesResponse\": {\"availableTime\": ", "");
            String finalString = removeBeginning.replace("uuid", "id");
            return finalString;

        } catch (JSONException e){
            return "Error while converting XML to JSON";
        }
    }

    public void setVehicleTypes(AvailableTimesDTO a, int workshopID) {
        a.setVehicleTypeServiced(tireChangeRepository.getVehicleTypeForWorkshop(workshopID));
    }

    public void setWorkshopName(AvailableTimesDTO a, int workshopID) {
        a.setWorkshopName(tireChangeRepository.getWorkshopName(workshopID));
    }

    public void setWorkshopAddress(AvailableTimesDTO a, int workshopID) {
        a.setWorkshopAdress(tireChangeRepository.getWorkshopAddress(workshopID));
    }

    public String bookTime(String workshopName, String bookID) {
        int workshopID = tireChangeRepository.getWorkshopID(workshopName);
        switch (workshopID) {
            case 1: return bookAtAPI1(bookID);
            case 2: return bookAtAPI2(bookID);
        }
        return "Broneerimine ei õnnestunud";
    }

    public String bookAtAPI1(String bookID) {
        try {
            String url = "http://localhost:9003/api/v1/tire-change-times/" + bookID + "/booking";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<london.tireChangeBookingRequest>\n" +
                    "\t<contactInformation>string</contactInformation>\n" +
                    "</london.tireChangeBookingRequest>";
            HttpEntity<String> entity = new HttpEntity<String>(xmlString, headers);
            this.restTemplate.put(url, entity);
            return "Aeg broneeritud";
        } catch (Exception e) {
            return "Broneerimine ei õnnestunud. ";
        }
    }

    public String bookAtAPI2(String bookID) {
        try {
            String url = "http://localhost:9004/api/v2/tire-change-times/" + bookID + "/booking";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            Map<String, Object> map = new HashMap<>();
            map.put("contactInformation", "string");
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = this.restTemplate.postForEntity(url, entity, String.class);
            return "Aeg broneeritud";
        } catch (Exception e) {
            return "Broneerimine ei õnnestunud";
        }
    }
}
