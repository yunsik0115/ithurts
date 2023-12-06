package com.sidepj.ithurts.controller.REST;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sidepj.ithurts.controller.REST.jsonDTO.HospitalJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.PharmacyJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.StatusCode;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.HospitalJSON;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.PharmacyJSON;
import com.sidepj.ithurts.controller.dto.ReportForm;
import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.domain.HospitalOfficeTime;
import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.domain.PharmacyOfficeTime;
import com.sidepj.ithurts.repository.ReportRepository;
import com.sidepj.ithurts.service.DataService;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.dto.PharmacyControllerDTO;
import com.sidepj.ithurts.service.naverapiservice.GeoLocationResponse;
import com.sidepj.ithurts.service.naverapiservice.NaverAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/json/maps")
@RequiredArgsConstructor
public class MapJSONController {

    private final DataService<Hospital> hospitalService;
    private final DataService<Pharmacy> pharmacyService;
    private final ReportRepository reportRepository;
    private final NaverAPIService naverAPIService;

    @ResponseBody
    @PostMapping("/search/retrieveAll")
    public String retrieveAll(){
        hospitalService.retrieveAll();
        return "OK";
    }


    // TO - DO Search Hospital의 경우 유저의 위치를 중심으로 일정 반경 내의 Hospital을 가져온다.
    @ResponseBody
    @GetMapping("/search/hospitals")
    public ResponseEntity<HospitalJsonResponse> getHospitals(HttpServletRequest request, @RequestParam double radius) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        GeoLocationResponse location = naverAPIService.getLocation(request);
        List<Hospital> hospitals = hospitalService.searchByDetailedCity(location.getGeoLocation().getR1(), location.getGeoLocation().getR2());
        HospitalJsonResponse hospitalJsonResponse = new HospitalJsonResponse(StatusCode.OK, "정상 응답 : 전체 병원 목록 불러오기", createHospitalJsonListFromEntites(hospitals));
        return new ResponseEntity<>(hospitalJsonResponse, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/search/hospital/{hospitalId}")
    public ResponseEntity<HospitalJsonResponse> getHospital(@PathVariable Long hospitalId){
        Hospital hospital = hospitalService.findById(hospitalId);
        HospitalJsonResponse hospitalJsonResponse = new HospitalJsonResponse(StatusCode.OK, "정상 응답 : 단건 병원 불러오기", createHospitalJsonFromEntity(hospital));
        // TO-DO : Exception Handling : Optional 반환하여 예외처리 가능하도록 수정하자.
        return new ResponseEntity<>(hospitalJsonResponse, HttpStatus.OK);
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
    public ResponseEntity<PharmacyJsonResponse> getPharmacies(@RequestParam String city, @RequestParam String detailedCity, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        GeoLocationResponse location = naverAPIService.getLocation(request);
        List<Pharmacy> pharmacies = pharmacyService.searchByDetailedCity(location.getGeoLocation().getR1(), location.getGeoLocation().getR2());
        PharmacyJsonResponse pharmacyJsonResponse = new PharmacyJsonResponse(StatusCode.OK, "정상 응답 : 전체 약국 목록 불러오기", createPharmaciesJSONFromEntities(pharmacies));
        return new ResponseEntity<>(pharmacyJsonResponse, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/search/pharmacy/{pharmacyId}")
    public ResponseEntity<PharmacyJsonResponse> getPharmacy(@PathVariable Long pharmacyId){
        Pharmacy pharmacy = pharmacyService.findById(pharmacyId);
        PharmacyJsonResponse pharmacyJsonResponse = new PharmacyJsonResponse(StatusCode.OK, "정상 응답 : 단건 약국 불러오기", createPharmacyJsonFromEntity(pharmacy));
        return new ResponseEntity<>(pharmacyJsonResponse, HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/search/pharmacy/{pharmacyId}/report")
    public ResponseEntity<PharmacyControllerDTO> reportPharamcy(@PathVariable Long pharmacyId, @RequestBody ReportForm reportForm){
        //Report report = new Report();
//        if(reportForm.getReportType().equals("hospital")){
//
//        }
//        else if(reportForm.getReportType().equals("pharmacy")){
//
//        } else{
//            throw new IllegalArgumentException("신고 대상이 조회되지 않습니다, 관리자에게 문의하세요");
//        }
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    private HospitalJSON createHospitalJsonFromEntity(Hospital hospital){
        HospitalJSON hospitalJSON = new HospitalJSON();
        hospitalJSON.setHospitalId(hospital.getId());
        hospitalJSON.setName(hospital.getName());
        hospitalJSON.setHospitalType(hospital.getHospitalType());
        hospitalJSON.setCreatedDate(hospital.getCreatedDate());
        if(hospital.getUpdatedDate() != null){
            hospitalJSON.setUpdateDate(hospital.getUpdatedDate());
        }
        injectHospitalOfficeTimes(hospital, hospitalJSON);
        return hospitalJSON;
    }

    private List<HospitalJSON> createHospitalJsonListFromEntites(List<Hospital> hospitals){
        List<HospitalJSON> hospitalJsons = new ArrayList<>();
        for (Hospital hospital : hospitals) {
            hospitalJsons.add(createHospitalJsonFromEntity(hospital));
        }
        return hospitalJsons;
    }

    private void injectHospitalOfficeTimes(Hospital hospital, HospitalJSON hospitalJSON){
        List<HospitalOfficeTime> officeTimes = hospital.getOfficeTimes();
        for (HospitalOfficeTime officeTime : officeTimes) {
            if(officeTime.getWeekday().equals("monday")){
                hospitalJSON.setMonOpen(officeTime.getStartOffice());
                hospitalJSON.setMonClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("tuesday")){
                hospitalJSON.setTueOpen(officeTime.getStartOffice());
                hospitalJSON.setTueClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("wednesday")){
                hospitalJSON.setWedOpen(officeTime.getStartOffice());
                hospitalJSON.setWedClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("thursday")){
                hospitalJSON.setThuOpen(officeTime.getStartOffice());
                hospitalJSON.setThuClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("friday")){
                hospitalJSON.setFriOpen(officeTime.getStartOffice());
                hospitalJSON.setFriClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("satsun")){
                hospitalJSON.setSatSunOpen(officeTime.getStartOffice());
                hospitalJSON.setSatSunClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("holiday")){
                hospitalJSON.setHolidayOpen(officeTime.getStartOffice());
                hospitalJSON.setHolidayClosed(officeTime.getEndOffice());
            }
        }
    }

    private PharmacyJSON createPharmacyJsonFromEntity(Pharmacy pharmacy){
        PharmacyJSON pharmacyJSON = new PharmacyJSON();
        pharmacyJSON.setPharmacyId(pharmacy.getId());
        pharmacyJSON.setName(pharmacy.getName());
        pharmacyJSON.setCreatedDate(pharmacy.getCreatedDate());
        if(pharmacy.getUpdatedDate() != null){
            pharmacyJSON.setUpdateDate(pharmacy.getUpdatedDate());
        }
        injectPharmacyOfficeTimes(pharmacy, pharmacyJSON);
        return pharmacyJSON;
    }

    private List<PharmacyJSON> createPharmaciesJSONFromEntities(List<Pharmacy> pharmacies){
        List<PharmacyJSON> pharmacyJSONS = new ArrayList<>();
        for (Pharmacy pharmacy : pharmacies) {
            pharmacyJSONS.add(createPharmacyJsonFromEntity(pharmacy));
        }
        return pharmacyJSONS;
    }

    private void injectPharmacyOfficeTimes(Pharmacy pharmacy, PharmacyJSON pharmacyJSON){
        List<PharmacyOfficeTime> officeTimes = pharmacy.getOfficeTimes();
        for (PharmacyOfficeTime officeTime : officeTimes) {
            if(officeTime.getWeekday().equals("monday")){
                pharmacyJSON.setMonOpen(officeTime.getStartOffice());
                pharmacyJSON.setMonClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("tuesday")){
                pharmacyJSON.setTueOpen(officeTime.getStartOffice());
                pharmacyJSON.setTueClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("wednesday")){
                pharmacyJSON.setWedOpen(officeTime.getStartOffice());
                pharmacyJSON.setWedClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("thursday")){
                pharmacyJSON.setThuOpen(officeTime.getStartOffice());
                pharmacyJSON.setThuClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("friday")){
                pharmacyJSON.setFriOpen(officeTime.getStartOffice());
                pharmacyJSON.setFriClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("satsun")){
                pharmacyJSON.setSatSunOpen(officeTime.getStartOffice());
                pharmacyJSON.setSatSunClosed(officeTime.getEndOffice());
            }
            if(officeTime.getWeekday().equals("holiday")){
                pharmacyJSON.setHolidayOpen(officeTime.getStartOffice());
                pharmacyJSON.setHolidayClosed(officeTime.getEndOffice());
            }
        }
    }




}
