package com.sidepj.ithurts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/maps")
public class MapController {

    @GetMapping("/")
    public String getMap(){
        return "map.html";
    }

    @GetMapping("/search")
    public String searchMap(){
        return "map.html";
    }

    @GetMapping("/search/hospitals")
    public String getHospitals(){
        return "map.html";
    }

    @GetMapping("/search/hospitals/{hospitalId}")
    public String getHospital(){
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
    public String getPharmacies(){
        return "map.html";
    }

    @GetMapping("/search/pharmacies/{pharmacyId}")
    public String getPharmacy(){
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
