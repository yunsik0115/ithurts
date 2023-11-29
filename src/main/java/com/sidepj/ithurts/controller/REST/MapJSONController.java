package com.sidepj.ithurts.controller.REST;


import com.sidepj.ithurts.controller.dto.ReportForm;
import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.domain.Report;
import com.sidepj.ithurts.repository.ReportRepository;
import com.sidepj.ithurts.service.DataService;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.dto.PharmacyControllerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/json/maps")
@RequiredArgsConstructor
public class MapJSONController {

    private final DataService<HospitalControllerDTO> hospitalService;
    private final DataService<PharmacyControllerDTO> pharmacyService;
    private final ReportRepository reportRepository;

    @ResponseBody
    @PostMapping("/search/retrieveAll")
    public String retrieveAll(){
        hospitalService.retrieveAll();
        return "OK";
    }


    // TO - DO Search Hospital의 경우 유저의 위치를 중심으로 일정 반경 내의 Hospital을 가져온다.
    @ResponseBody
    @GetMapping("/search/hospitals")
    public ResponseEntity<List<HospitalControllerDTO>> getHospitals(@RequestParam String city, @RequestParam String detailedCity){
        List<HospitalControllerDTO> hospitalControllerDTOS = hospitalService.searchByDetailedCity(city, detailedCity);
        return new ResponseEntity<>(hospitalControllerDTOS, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/search/hospital/{hospitalId}")
    public ResponseEntity<HospitalControllerDTO> getHospital(@PathVariable Long hospitalId){
        HospitalControllerDTO byId = hospitalService.findById(hospitalId);
        // TO-DO : Exception Handling : Optional 반환하여 예외처리 가능하도록 수정하자.
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @ResponseBody
    @PatchMapping("/search/hospitals/refresh")
    public ResponseEntity<List<HospitalControllerDTO>> refreshHospitals(){ // @RequestBody List<Long> hospitalIds
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/search/hospital/{hospitalId}/report")
    public ResponseEntity<HospitalControllerDTO> reportHospital(@PathVariable Long hospitalId, @RequestBody ReportForm reportForm){ // @RequestBody List<Long> hospitalIds

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/search/pharmacies")
    public ResponseEntity<List<PharmacyControllerDTO>> getPharmacies(@RequestParam String city, @RequestParam String detailedCity){
        List<PharmacyControllerDTO> pharmacyControllerDTOS = pharmacyService.searchByDetailedCity(city, detailedCity);
        return new ResponseEntity<>(pharmacyControllerDTOS, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/search/pharmacy/{pharmacyId}")
    public ResponseEntity<PharmacyControllerDTO> getPharmacy(@PathVariable Long pharmacyId){
        PharmacyControllerDTO pharmacy = pharmacyService.findById(pharmacyId);
        return new ResponseEntity<>(pharmacy, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/search/pharmacy/{pharmacyId}/report")
    public ResponseEntity<PharmacyControllerDTO> reportPharamcy(@PathVariable Long pharmacyId, @RequestBody ReportForm reportForm){
        //Report report = new Report();
        return  new ResponseEntity<>(HttpStatus.OK);
    }




}
