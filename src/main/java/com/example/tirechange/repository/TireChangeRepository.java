package com.example.tirechange.repository;

import com.example.tirechange.service.WorkshopDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TireChangeRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<String> getWorkshopList () {
        String sql = "SELECT workshop_name FROM workshops";
        Map<String, Object> paramMap = new HashMap<>();
        List<String> workshopList = jdbcTemplate.queryForList(sql, paramMap, String.class);
        workshopList.add(0, "Kõik töökojad");
        return workshopList;
    }

    public List<String> getWorkshopData (String workshopName) {
        String sql = "SELECT (workshop_name, workshop_address) FROM workshops WHERE workshop_name = :wn";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("wn", workshopName);
        return jdbcTemplate.queryForList(sql, paramMap, String.class);

    }
    public List<String> getVehicleTypeList () {
        String sql = "SELECT vehicle_type_name FROM vehicle_types";
        Map<String, Object> paramMap = new HashMap<>();
        List<String> vehicleTypeList = jdbcTemplate.queryForList(sql, paramMap, String.class);
        vehicleTypeList.add(0, "Kõik sõiduki tüübid");
        return vehicleTypeList;
    }

    public void addNewWorkshopNameAddress(WorkshopDTO workshopDTO) {
        String sql = "INSERT INTO workshops (workshop_name, workshop_address) VALUES (:name, :adress)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", workshopDTO.getWorkshopName());
        paramMap.put("adress", workshopDTO.getWorkshopAddress());
        jdbcTemplate.update(sql, paramMap);
    }

    public void addServicedVehiclesToWorkshop(int workshopID, int vehicleTypeID) {
        String sql = "INSERT INTO workshop_vehicles_join (workshop_id, vehicletype_type_id) VALUES (:wID, :vID)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("wID", workshopID);
        paramMap.put("wID", vehicleTypeID);
        jdbcTemplate.update(sql, paramMap);
    }

    public void updateWorkshopNameAddress(int workshopID, String updatedWorkshopName, String updatedWorkshopAddress) {
        String sql = "UPDATE workshops SET workshop_name = :wn, workshop_address = :wa WHERE workshop_id = :wID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("wn", updatedWorkshopName);
        paramMap.put("wa", updatedWorkshopAddress);
        paramMap.put("wID", workshopID);
        jdbcTemplate.update(sql, paramMap);
    }

    public void deleteWorkshopServicedVehicles(int workshopID) {
        String sql = "DELETE FROM workshops_vehicles_join WHERE workshop_id = :wID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("wID", workshopID);
        jdbcTemplate.update(sql, paramMap);
    }

    public int getWorkshopID(String workshopName) {
        String sql = "SELECT workshop_id FROM workshops WHERE workshop_name = :wName";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("wName", workshopName);
        return jdbcTemplate.queryForObject(sql, paramMap, Integer.class);
    }

    public int getVehicleTypeID(String vehicleTypeName) {
        String sql = "SELECT vehicle_type_id FROM vehicle_types WHERE vehicle_type_name = :vtName";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("vtName", vehicleTypeName);
        return jdbcTemplate.queryForObject(sql, paramMap, Integer.class);
    }

    public String getWorkshopName(int workshopID) {
        String sql = "SELECT workshop_name FROM workshops WHERE workshop_id = :wID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("wID", workshopID);
        return jdbcTemplate.queryForObject(sql, paramMap, String.class);
    }

    public String getWorkshopAddress(int workshopID) {
        String sql = "SELECT workshop_address FROM workshops WHERE workshop_id = :wID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("wID", workshopID);
        return jdbcTemplate.queryForObject(sql, paramMap, String.class);
    }

    public List<String> getVehicleTypeForWorkshop (int workshopID) {
        String sql = "SELECT vehicle_type_name FROM vehicle_types vt JOIN workshops_vehicles_join wvt ON vt.vehicle_type_id = wvt.vehicle_type_id JOIN workshops w ON wvt.workshop_id = w.workshop_id WHERE w.workshop_id = :wID";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("wID", workshopID);
        return jdbcTemplate.queryForList(sql, paramMap, String.class);
    }
}
