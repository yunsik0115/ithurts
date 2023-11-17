package com.sidepj.ithurts.controller;

import com.sidepj.ithurts.service.HospitalService;
import com.sidepj.ithurts.service.PharmacyService;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.dto.PharmacyControllerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@Controller
@RequestMapping("/maps")
@RequiredArgsConstructor
public class MapController {

    private final HospitalService hospitalService;
    private final PharmacyService pharmacyService;

    @GetMapping("/")
    public String getMap(){
        return "map.html";
    }

    @GetMapping("/search")
    public String searchMap(@RequestParam String name, Model model){
        // TO - DO, 검색시 검색 범위를 해당 사용자가 위치한 Map의 동으로 제한한다. //
        List<HospitalControllerDTO> hospitalControllerDTOS = hospitalService.searchByName(name);
        List<PharmacyControllerDTO> pharmacyControllerDTOS = pharmacyService.searchByName(name);
        model.addAttribute("hospitals", hospitalControllerDTOS);
        model.addAttribute("pharmacies", pharmacyControllerDTOS);
        return "map.html";
    }

    @GetMapping("/search/hospitals")
    public String getHospitals(@RequestParam String city, @RequestParam String detailedCity){
        hospitalService.searchByDetailedCity(city, detailedCity);
        // city, detailed city will be provided by Naver API GeoLocation Service.
        return "map.html";
    }

    @GetMapping("/search/hospitals/{hospitalId}")
    public String getHospital(@PathVariable Long hospitalId, Model model){
        HospitalControllerDTO hospital = hospitalService.findById(hospitalId);
        model.addAttribute("hospital", hospital);
        return "map.html";
    }

    @PostMapping("/search/hospitals/refresh")
    public String refreshHospitals(){
        return "map.html"; //ASYNC?
    }

    @GetMapping("/search/hospitals/{hospitalId}/report")
    public String reportHospitalForm(){
        return "reportForm.html";
    }

    @PostMapping("/search/hospitals/{hospitalId}/report")
    public String reportHospital(){
        return "map.html";
    }

    @GetMapping("/search/pharmacies")
    public String getPharmacies(@RequestParam String city, @RequestParam String detailedCity, Model model){
        List<PharmacyControllerDTO> pharmacyControllerDTOS = pharmacyService.searchByDetailedCity(city, detailedCity);
        model.addAttribute("pharmacies", pharmacyControllerDTOS);
        return "map.html";
    }

    @GetMapping("/search/pharmacies/{pharmacyId}")
    public String getPharmacy(@PathVariable Long pharmacyId, Model model){
        PharmacyControllerDTO pharmacy = pharmacyService.findById(pharmacyId);
        model.addAttribute("pharmacy", pharmacy);
        return "map.html";
    }

    @PostMapping("/search/pharmacies/refresh")
    public String refreshPharmacies(){
        return "map.html";
    }

    @GetMapping("/search/pharmacies/{pharmacyId}/report")
    public String reportPharmacyForm(){
        return "reportForm.html";
    }

    @PostMapping("/search/pharmacies/{pharmacyId}/report")
    public String reportPharmacy(){
        return "map.html";
    }
}
