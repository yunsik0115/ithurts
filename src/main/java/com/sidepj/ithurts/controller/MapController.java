package com.sidepj.ithurts.controller;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.service.DataService;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.dto.PharmacyControllerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/maps")
@RequiredArgsConstructor
public class MapController {

    //private final HospitalService hospitalService;
    private final DataService<Hospital> hospitalService;
    //private final PharmacyService pharmacySevice;
    private final DataService<Pharmacy> pharmacyService;

    @GetMapping("/")
    public String getMap(){
        return "map.html";
    }

    @GetMapping("/search")
    public String searchMap(@RequestParam String name, Model model){
        // TO - DO, 검색시 검색 범위를 해당 사용자가 위치한 Map의 동으로 제한한다. //
        List<HospitalControllerDTO> hospitalControllerDTOS = hospitalTransferToDTOS(hospitalService.searchByName(name));
        List<PharmacyControllerDTO> pharmacyControllerDTOS = pharmacyTransferToDTOS(pharmacyService.searchByName(name));
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
        HospitalControllerDTO hospital = hospitalEntityDtoTransferAndValidation(hospitalService.findById(hospitalId));
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
        List<PharmacyControllerDTO> pharmacyControllerDTOS = pharmacyTransferToDTOS(pharmacyService.searchByDetailedCity(city, detailedCity));
        model.addAttribute("pharmacies", pharmacyControllerDTOS);
        return "map.html";
    }

    @GetMapping("/search/pharmacies/{pharmacyId}")
    public String getPharmacy(@PathVariable Long pharmacyId, Model model){
        PharmacyControllerDTO pharmacy = pharmacyEntityDtoTransferAndValidation(pharmacyService.findById(pharmacyId));
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

    public List<HospitalControllerDTO> hospitalTransferToDTOS(List<Hospital> hospitalList){
        List<HospitalControllerDTO> list = new ArrayList<>();
        for (Hospital hospital : hospitalList) {
            list.add(hospitalEntityDtoTransferAndValidation(hospital));
        }
        return list;
    }

    public HospitalControllerDTO hospitalEntityDtoTransferAndValidation(Hospital entity){
        HospitalControllerDTO dto = new HospitalControllerDTO();
        if(entity.getId() != null){
            dto.setId(entity.getId());
        }

        if(entity.getName() != null){
            dto.setName(entity.getName());
        }

        if(entity.getContact() != null){
            dto.setContact(entity.getContact());
        }

        if(entity.getAddress() != null){
            dto.setAddress(entity.getAddress());
        }

        if(StringUtils.hasText(entity.getHospitalType())){
            dto.setHospitalType(entity.getHospitalType());
        }

        if(entity.getCoordinates() != null){
            dto.setX_cord(entity.getCoordinates().getX());
            dto.setY_cord(entity.getCoordinates().getY());
        }

        if(entity.getCreatedDate() != null){
            dto.setCreatedDate(entity.getCreatedDate());
        }

        if(entity.getUpdatedDate() != null){
            dto.setUpdatedDate(entity.getUpdatedDate());
        }

        return dto;
    }

    public List<PharmacyControllerDTO> pharmacyTransferToDTOS(List<Pharmacy> pharmacyList){
        List<PharmacyControllerDTO> list = new ArrayList<>();
        for (Pharmacy pharmacy : pharmacyList) {
            list.add(pharmacyEntityDtoTransferAndValidation(pharmacy));
        }
        return list;
    }

    public PharmacyControllerDTO pharmacyEntityDtoTransferAndValidation(Pharmacy entity){
        PharmacyControllerDTO dto = new PharmacyControllerDTO();
        if(entity.getName() != null){
            dto.setName(entity.getName());
        }

        if(entity.getContact() != null){
            dto.setContact(entity.getContact());
        }

        if(entity.getAddress() != null){
            dto.setAddress(entity.getAddress());
        }

        if(entity.getCoordinates() != null){
            dto.setX_cord(entity.getCoordinates().getX());
            dto.setY_cord(entity.getCoordinates().getY());
        }

        if(entity.getCreatedDate() != null){
            dto.setCreatedDate(LocalDateTime.now());
        }

        dto.setUpdatedDate(LocalDateTime.now());

        return dto;
    }
}
