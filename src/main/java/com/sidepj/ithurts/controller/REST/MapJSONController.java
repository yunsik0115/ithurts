package com.sidepj.ithurts.controller.REST;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.sidepj.ithurts.controller.REST.jsonDTO.HospitalJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.NaverMapAPISearchResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.PharmacyJsonResponse;
import com.sidepj.ithurts.controller.REST.jsonDTO.StatusCode;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.HospitalJSON;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.PharmacyJSON;
import com.sidepj.ithurts.controller.dto.ReportDTO;
import com.sidepj.ithurts.domain.*;
import com.sidepj.ithurts.repository.ReportRepository;
import com.sidepj.ithurts.service.DataService;
import com.sidepj.ithurts.service.ReportService;
import com.sidepj.ithurts.service.SessionConst;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.dto.PharmacyControllerDTO;
import com.sidepj.ithurts.service.naverapiservice.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.AccessDeniedException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/json/maps")
@RequiredArgsConstructor
@Slf4j
public class MapJSONController {

    private final DataService<Hospital> hospitalService;
    private final DataService<Pharmacy> pharmacyService;
    private final ReportService reportService;
    private final NaverAPIUtils naverAPIUtils;
    private final NaverAPIService naverAPIService;

    @ResponseBody
    @PostMapping("/search/retrieveAll")
    public String retrieveAll(){
        hospitalService.retrieveAll();
        pharmacyService.retrieveAll();
        return "OK";
    }


    // TO - DO Search Hospital의 경우 유저의 위치를 중심으로 일정 반경 내의 Hospital을 가져온다.
    @ResponseBody
    @GetMapping("/search/hospitals/bsdlocation")
    public ResponseEntity<HospitalJsonResponse> getHospitals(HttpServletRequest request,
                                                             @RequestParam double radius,
                                                             Pageable pageable) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        GeoLocationResponse location = naverAPIUtils.getLocation(request);
        //List<Hospital> hospitals = hospitalService.searchByDetailedCity(location.getGeoLocation().getR1(), location.getGeoLocation().getR2());
        List<Hospital> hospitals = hospitalService.searchByCoordinateAndRadius(location.getGeoLocation().getLon(), location.getGeoLocation().getLat(), radius);
        HospitalJsonResponse hospitalJsonResponse = new HospitalJsonResponse(StatusCode.OK, "정상 응답 : 전체 병원 목록 불러오기", createHospitalJsonListFromEntites(hospitals));
        return new ResponseEntity<>(hospitalJsonResponse, HttpStatus.OK);
    }

    // TO - DO Search Hospital의 경우 유저의 위치를 중심으로 일정 반경 내의 Hospital을 가져온다.
    @ResponseBody
    @GetMapping("/search/hospitals/bsdlocation/now")
    public ResponseEntity<HospitalJsonResponse> getHospitalsOnWork(HttpServletRequest request,
                                                                   @RequestParam double radius,
                                                                   @RequestParam boolean holiday,
                                                                   Pageable pageable) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        GeoLocationResponse location = naverAPIUtils.getLocation(request);
        //List<Hospital> hospitals = hospitalService.searchByDetailedCity(location.getGeoLocation().getR1(), location.getGeoLocation().getR2());
        //List<Hospital> hospitals = hospitalService.searchByCoordinateAndRadius(location.getGeoLocation().getLon(), location.getGeoLocation().getLat(), radius);
        List<Hospital> hospitals = hospitalService.searchOpened(location.getGeoLocation().getLon(), location.getGeoLocation().getLat(), radius, holiday);
        HospitalJsonResponse hospitalJsonResponse = new HospitalJsonResponse(StatusCode.OK, "정상 응답 : 전체 병원 목록 불러오기", createHospitalJsonListFromEntites(hospitals));
        return new ResponseEntity<>(hospitalJsonResponse, HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/search/pharmacies/bsdlocation")
    public ResponseEntity<PharmacyJsonResponse> getPharmacies(HttpServletRequest request,
                                                              @RequestParam double radius,
                                                              Pageable pageable) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        GeoLocationResponse location = naverAPIUtils.getLocation(request);
        //List<Pharmacy> pharmacies = pharmacyService.searchByDetailedCity(location.getGeoLocation().getR1(), location.getGeoLocation().getR2());
        List<Pharmacy> pharmacies = pharmacyService.searchByCoordinateAndRadius(location.getGeoLocation().getLon(), location.getGeoLocation().getLat(), radius);
        PharmacyJsonResponse pharmacyJsonResponse = new PharmacyJsonResponse(StatusCode.OK, "정상 응답 : 전체 약국 목록 불러오기", createPharmacyJsonListFromEntites(pharmacies));
        return new ResponseEntity<>(pharmacyJsonResponse, HttpStatus.OK);
    }

    // TO - DO Search Hospital의 경우 유저의 위치를 중심으로 일정 반경 내의 Hospital을 가져온다.
    @ResponseBody
    @GetMapping("/search/pharmacies/bsdlocation/now")
    public ResponseEntity<PharmacyJsonResponse> getPharmaciesOnWork(HttpServletRequest request,
                                                                    @RequestParam double radius,
                                                                    @RequestParam boolean holiday,
                                                                    Pageable pageable) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        GeoLocationResponse location = naverAPIUtils.getLocation(request);
        //List<Hospital> hospitals = hospitalService.searchByDetailedCity(location.getGeoLocation().getR1(), location.getGeoLocation().getR2());
        //List<Hospital> hospitals = hospitalService.searchByCoordinateAndRadius(location.getGeoLocation().getLon(), location.getGeoLocation().getLat(), radius);
        List<Pharmacy> pharmacies = pharmacyService.searchOpened(location.getGeoLocation().getLon(), location.getGeoLocation().getLat(), radius, holiday);
        PharmacyJsonResponse pharmacyJsonResponse = new PharmacyJsonResponse(StatusCode.OK, "정상 응답 : 전체 병원 목록 불러오기", createPharmacyJsonListFromEntites(pharmacies));
        return new ResponseEntity<>(pharmacyJsonResponse, HttpStatus.OK);
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
    @GetMapping("/search/hospital/{hospitalId}/naver")
    public ResponseEntity<NaverMapAPISearchResponse> getHospitalMetaFromNaver(@PathVariable Long hospitalId, HttpServletRequest request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, InterruptedException, MalformedURLException {
        GeoLocationResponse location = naverAPIUtils.getLocation(request);
        GeoLocation geoLocation = location.getGeoLocation();
        log.info("geolocation = {}", geoLocation);

        Hospital byId = hospitalService.findById(hospitalId);
        Point coordinates = byId.getCoordinates();
        log.info("x = {}", coordinates.getX());
        log.info("y = {}", coordinates.getY());
        NaverMapAPISearchResult searchResult = naverAPIUtils.getSearchResult(byId.getName(), coordinates.getY(), coordinates.getX());
        URL locationMetaInfoURL = getLocationMetaInfoURL(searchResult.getSid());

        //naverAPIService.getOfficeTime(byId.getName(), coordinates.getY(), coordinates.getX());
        return new ResponseEntity<>(new NaverMapAPISearchResponse(StatusCode.OK, "정상 응답 : 성공적으로 네이버 URL을 가져왔습니다",searchResult, locationMetaInfoURL), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/search/hospital/{hospitalId}/report")
    public ResponseEntity<ReportDTO> reportHospital(@PathVariable Long hospitalId, @RequestBody ReportDTO reportDTO, HttpServletRequest request) throws AccessDeniedException, IllegalAccessException { // @RequestBody List<Long> hospitalIds
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null){
            throw new AccessDeniedException("로그인이 필요합니다");
        }

        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);
        reportService.save(reportDTO, member.getId(), hospitalId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/search/pharmacies")
    public ResponseEntity<PharmacyJsonResponse> getPharmacies(HttpServletRequest request,
                                                              Pageable pageable) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        GeoLocationResponse location = naverAPIUtils.getLocation(request);
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
    @GetMapping("/search/pharmacy/{pharmacyId}/naver")
    public ResponseEntity<NaverMapAPISearchResponse> getPharmacyMetaFromNaver(@PathVariable Long pharmacyId, HttpServletRequest request)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException, InterruptedException, MalformedURLException {
        GeoLocationResponse location = naverAPIUtils.getLocation(request);
        GeoLocation geoLocation = location.getGeoLocation();
        log.info("geolocation = {}", geoLocation);

        Pharmacy byId = pharmacyService.findById(pharmacyId);
        Point coordinates = byId.getCoordinates();
        log.info("x = {}", coordinates.getX());
        log.info("y = {}", coordinates.getY());
        NaverMapAPISearchResult searchResult = naverAPIUtils.getSearchResult(byId.getName(), coordinates.getY(), coordinates.getX());
        URL locationMetaInfoURL = getLocationMetaInfoURL(searchResult.getSid());

        //naverAPIService.getOfficeTime(byId.getName(), coordinates.getY(), coordinates.getX());
        return new ResponseEntity<>(new NaverMapAPISearchResponse(StatusCode.OK, "정상 응답 : 성공적으로 네이버 URL을 가져왔습니다",searchResult, locationMetaInfoURL), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping("/search/pharmacy/{pharmacyId}/report")
    public ResponseEntity<PharmacyControllerDTO> reportPharamcy(@PathVariable Long pharmacyId, @RequestBody ReportDTO reportDTO, HttpServletRequest request) throws AccessDeniedException, IllegalAccessException {
        HttpSession httpSession = request.getSession();
        if(httpSession.getAttribute(SessionConst.LOGIN_MEMBER) == null){
            throw new AccessDeniedException("로그인이 필요합니다");
        }

        Member member = (Member) httpSession.getAttribute(SessionConst.LOGIN_MEMBER);
        reportService.save(reportDTO, member.getId(), pharmacyId);
        return new ResponseEntity<>(HttpStatus.OK);
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
        hospitalJSON.setCoord_x(hospital.getCoordinates().getX());
        hospitalJSON.setCoord_y(hospital.getCoordinates().getY());
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
        pharmacyJSON.setCoord_x(pharmacy.getCoordinates().getX());
        pharmacyJSON.setCoord_y(pharmacy.getCoordinates().getY());
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

    private List<PharmacyJSON> createPharmacyJsonListFromEntites(List<Pharmacy> pharmacies){
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

    private URL getLocationMetaInfoURL(String sid) throws MalformedURLException {
        final String hostname = "https://pcmap.place.naver.com";
        final String requestUrl = "/hospital/" + sid +"/home";
        return new URL(hostname + requestUrl);
    }




}
