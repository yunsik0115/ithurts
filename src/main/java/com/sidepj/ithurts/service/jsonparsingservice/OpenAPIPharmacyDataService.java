package com.sidepj.ithurts.service.jsonparsingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sidepj.ithurts.domain.PharmacyOfficeTime;
import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.repository.PharmacyOfficeTimeRepository;
import com.sidepj.ithurts.repository.PharmacyRepository;
import com.sidepj.ithurts.service.dto.jsonparsingdto.PharmacyDTO;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:API-KEY.yml")
public class OpenAPIPharmacyDataService implements OpenAPIDataService<Pharmacy>{

    private final PharmacyRepository pharmacyRepository;
    private final PharmacyOfficeTimeRepository pharmacyOfficeTimeRepository;
    private final ObjectMapper objectMapper;

    @Value("${OPENAPI-Pharmacy-SecretKey}") // Lombok의 Value가 아님
    private String serviceKeyValue;

    @Override
    public List<Pharmacy> retrieve(SearchCondition SearchCondition) {
        log.trace("====== Start Retrieving Pharmacy Data From OPENAPI ======");

        JSONObject xmlJSONObj;
        try {
            xmlJSONObj = getJsonObject(SearchCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JSONArray pharmacies;
        Object itemObj = xmlJSONObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").get("item");

        pharmacies = jsonObjectArrayDeterminator(itemObj);

        JsonMapper jsonMapper = JsonMapper.builder().build();
        List<PharmacyDTO> pharmacyDTOList = new ArrayList<>();

        log.trace("===========PharmacyDTOs Creation============");
        for(int i = 0; i < pharmacies.length(); i++){
            JSONObject pharmacy = pharmacies.getJSONObject(i);
            String pharmacyJSONString = pharmacy.toString();
            PharmacyDTO pharmacyDTO = null;
            try {
                pharmacyDTO = jsonMapper.readValue(pharmacyJSONString, PharmacyDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            pharmacyDTOList.add(pharmacyDTO);
            log.trace("{}", pharmacyDTO);
        }

        log.trace("============Pharmacy DTO Entity Transformation Completed ============");

        return save(dtosToPharmacy(pharmacyDTOList));
    }

    private static JSONArray jsonObjectArrayDeterminator(Object itemObj) {
        JSONArray pharmacies;
        if(itemObj instanceof JSONArray){
            pharmacies = (JSONArray) itemObj;
        } else if(itemObj instanceof JSONObject){
            pharmacies = new JSONArray();
            pharmacies.put((JSONObject) itemObj);
        } else{
            throw new JSONException("item key is neither a JSONObject nor a JSONArray");
        }
        return pharmacies;
    }

    private JSONObject getJsonObject(SearchCondition SearchCondition) throws IOException {
        StringBuilder urlBuilder = getUrlBySearchCondition(SearchCondition);
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        log.trace("Response code: {}" , conn.getResponseCode());
        log.trace("url = {}", url);

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        JSONObject xmlJSONObj = XML.toJSONObject(sb.toString());
        return xmlJSONObj;
    }

    private List<Pharmacy> save(List<Pharmacy> pharmacyList){
        List<Pharmacy> savedPharmaciesList = new ArrayList<>();
        for (Pharmacy pharmacy : pharmacyList) {
            if(pharmacyRepository.findPharmacyByNameAndAddress(pharmacy.getName(), pharmacy.getAddress()) == null) {
                savedPharmaciesList.add(pharmacyRepository.save(pharmacy));
                pharmacy.setCreatedDate(LocalDateTime.now());
                pharmacyOfficeTimeRepository.saveAll(pharmacy.getOfficeTimes());
            }
            else {
                Pharmacy pharmacyOnDB = pharmacyRepository.findPharmacyByNameAndAddress(pharmacy.getName(), pharmacy.getAddress());
                entityUpdateWithEntity(pharmacy, pharmacyOnDB);
                savedPharmaciesList.add(pharmacyOnDB);
            }
        }
        return savedPharmaciesList;
    }



    private List<Pharmacy> dtosToPharmacy(List<PharmacyDTO> pharmacyDTOS){
        log.trace("=================DTO TO PHARMACY TRANSFERRATION ==============");
        List<Pharmacy> pharmacyTransferedPharmacyList = new ArrayList<>();
        for (PharmacyDTO pharmacyDTO : pharmacyDTOS) {
            Pharmacy pharmacy = new Pharmacy();
            entityInjectionFromDTO(pharmacyDTO, pharmacy);
            officeTimeInjectionFromDTOs(pharmacyDTO, pharmacy);
            log.trace("{}", pharmacy);
            pharmacyTransferedPharmacyList.add(pharmacy);
        }
        log.trace("================DTO TO PHARMACY TRANSFERRATION FINISHED ==============");
        return pharmacyTransferedPharmacyList;
    }

    private void entityInjectionFromDTO(PharmacyDTO pharmacyDTO, Pharmacy pharmacy){
        log.trace(pharmacyDTO.getDutyName());
        if(pharmacyDTO.getDutyName() != null){
            pharmacy.setName(pharmacyDTO.getDutyName());
        }
        if(pharmacyDTO.getDutyTel1() != null){
            pharmacy.setContact(pharmacyDTO.getDutyTel1());
        }
        if(pharmacyDTO.getDutyAddr() != null){
            pharmacy.setAddress(pharmacyDTO.getDutyAddr());
        }
        if(pharmacyDTO.getWgs84Lat() != null && pharmacyDTO.getWgs84Lon() != null){
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
            Coordinate coordinate = new Coordinate(pharmacyDTO.getWgs84Lon(), pharmacyDTO.getWgs84Lat());
            Point point = geometryFactory.createPoint(coordinate);
            pharmacy.setCoordinates(point);
        }
        pharmacy.setCreatedDate(LocalDateTime.now());
    }

    private static void entityUpdateWithEntity(Pharmacy from, Pharmacy to){
        if(!from.getName().equals(to.getName())) {
            to.setName(from.getName());
        }
        if(!from.getAddress().equals(to.getAddress())) {
            to.setAddress(from.getAddress());
        }
        if(!from.getCoordinates().equals(to.getCoordinates())) {
            to.setCoordinates(from.getCoordinates());
        }
        if(!from.getContact().equals(to.getCoordinates())) {
            to.setContact(from.getContact());
        }
        if(!from.getOfficeTimes().equals(to.getOfficeTimes())) {
            to.setOfficeTimes(from.getOfficeTimes());
        }

    }

    private void officeTimeInjectionFromDTOs(PharmacyDTO pharmacyDTO, Pharmacy pharmacy){

        List<PharmacyOfficeTime> pharmacyOfficeTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm", Locale.KOREA);
        //c - 오후  s- 오전
        PharmacyOfficeTime mon = new PharmacyOfficeTime("monday", null, null, pharmacy);
        if(pharmacyDTO.getDutyTime1s() != null || pharmacyDTO.getDutyTime1c() != null) {
            LocalTime mondayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime1s(), formatter);
            LocalTime mondayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
            mon = new PharmacyOfficeTime("monday", mondayOPEN, mondayCLOSED, pharmacy);
        }
        //pharmacyOfficeTimeRepository.save(mon);
        pharmacy.addTime(mon);

        PharmacyOfficeTime tue = new PharmacyOfficeTime("tuesday", null, null, pharmacy);
        if(pharmacyDTO.getDutyTime2c() != null && pharmacyDTO.getDutyTime2s() != null) {
            LocalTime tuesdayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime2s(), formatter);
            LocalTime tuesdayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime2c(), formatter);
            tue = new PharmacyOfficeTime("tuesday", tuesdayOPEN, tuesdayCLOSED, pharmacy);
        }
        //pharmacyOfficeTimeRepository.save(tue);
        pharmacy.addTime(tue);

        PharmacyOfficeTime wed = new PharmacyOfficeTime("wednesday", null, null, pharmacy);
        if(pharmacyDTO.getDutyTime3s() != null && pharmacyDTO.getDutyTime3c() != null) {
            LocalTime wednesdayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime3s(), formatter);
            LocalTime wednesdayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime3c(), formatter);
            wed = new PharmacyOfficeTime("wednesday", wednesdayOPEN, wednesdayCLOSED, pharmacy);
        }
        //pharmacyOfficeTimeRepository.save(wed);
        pharmacy.addTime(wed);

        PharmacyOfficeTime thu = new PharmacyOfficeTime("thursday", null, null, pharmacy);
        if(pharmacyDTO.getDutyTime4s() != null && pharmacyDTO.getDutyTime4c() != null) {
            LocalTime thursdayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime4s(), formatter);
            LocalTime thursdayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime4c(), formatter);
            thu = new PharmacyOfficeTime("thursday", thursdayOPEN, thursdayCLOSED, pharmacy);
        }
        //pharmacyOfficeTimeRepository.save(thu);
        pharmacy.addTime(thu);

        PharmacyOfficeTime fri = new PharmacyOfficeTime("thursday", null, null, pharmacy);
        if(pharmacyDTO.getDutyTime5s() != null && pharmacyDTO.getDutyTime5c() != null) {
            LocalTime fridayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime5s(), formatter);
            LocalTime fridayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime5c(), formatter);
            fri = new PharmacyOfficeTime("friday", fridayOPEN, fridayCLOSED, pharmacy);
        }
        //pharmacyOfficeTimeRepository.save(fri);
        pharmacy.addTime(fri);

        PharmacyOfficeTime satSun = new PharmacyOfficeTime("satsun", null, null, pharmacy);
        if(pharmacyDTO.getDutyTime7s() != null && pharmacyDTO.getDutyTime7c() != null) {
            LocalTime satSunOpen = LocalTime.parse(pharmacyDTO.getDutyTime7s(), formatter);
            LocalTime satSunClosed = LocalTime.parse(pharmacyDTO.getDutyTime7c(), formatter);
            satSun = new PharmacyOfficeTime("satsun", satSunOpen, satSunClosed, pharmacy);
        }
        //pharmacyOfficeTimeRepository.save(satSun);
        pharmacy.addTime(satSun);

        PharmacyOfficeTime holiday = new PharmacyOfficeTime("holiday", null, null, pharmacy);
        if(pharmacyDTO.getDutyTime8s() != null && pharmacyDTO.getDutyTime8c() != null) {
            LocalTime holidayOpen = LocalTime.parse(pharmacyDTO.getDutyTime8s(), formatter);
            LocalTime holidayClosed = LocalTime.parse(pharmacyDTO.getDutyTime8c(), formatter);
            holiday = new PharmacyOfficeTime("holiday", holidayOpen, holidayClosed, pharmacy);
        }
        //pharmacyOfficeTimeRepository.save(holiday);
        pharmacy.addTime(holiday);

    }

    private StringBuilder getUrlBySearchCondition(SearchCondition searchCondition ) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire"); /*URL*/
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", "UTF-8")).append("=").append(serviceKeyValue); /*Service Key*/

        if(StringUtils.hasText(searchCondition.getCity())){ // 주소 (시도)
            urlBuilder.append("&").append(URLEncoder.encode("Q0", "UTF-8")).append("=").append(URLEncoder.encode(searchCondition.getCity(), "UTF-8"));
        }

        if(StringUtils.hasText(searchCondition.getDetailedCity())){ // 주소 (시군구)
            urlBuilder.append("&").append(URLEncoder.encode("Q1", "UTF-8")).append("=").append(URLEncoder.encode(searchCondition.getDetailedCity(), "UTF-8"));
        }

        if(StringUtils.hasText(searchCondition.getOfficeName())){ // 기관명
            urlBuilder.append("&").append(URLEncoder.encode("QN", "UTF-8")).append("=").append(URLEncoder.encode(searchCondition.getOfficeName(), "UTF-8"));
        }

        if(searchCondition.getOfficeDay() != null){ // 진료 요일
            urlBuilder.append("&").append(URLEncoder.encode("QT", "UTF-8")).append("=").append(URLEncoder.encode(String.valueOf(searchCondition.getOfficeDay()), "UTF-8"));
        }

        if(searchCondition.getOrder() != null){ // 순서
            urlBuilder.append("&").append(URLEncoder.encode("ORD", "UTF-8")).append("=").append(URLEncoder.encode(String.valueOf(searchCondition.getOrder()), "UTF-8"));
        }

        if(searchCondition.getPageNo() != null){ // 페이지 번호
            urlBuilder.append("&").append(URLEncoder.encode("pageNo", "UTF-8")).append("=").append(URLEncoder.encode(String.valueOf(searchCondition.getPageNo()), "UTF-8"));
        }

        if(searchCondition.getNumOfRows() != null){ // 받아올 건수
            urlBuilder.append("&").append(URLEncoder.encode("numOfRows", "UTF-8")).append("=").append(URLEncoder.encode(String.valueOf(searchCondition.getNumOfRows()), "UTF-8"));
        }

        return urlBuilder;
    }

    //    public Pharmacy retrieveOne(SearchCondition searchCondition){
//        log.trace("====== Start Retrieving Hospital Datas From OPENAPI ======");
//
//        JSONObject xmlJSONObj = null;
//        try {
//            xmlJSONObj = getJsonObject(searchCondition);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        log.trace("xmljsonobj = {}", xmlJSONObj);
//        JSONObject pharmacy = xmlJSONObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONObject("item");
//        JsonMapper jsonMapper = JsonMapper.builder().build();
//        String pharmacyJSONString = pharmacy.toString();
//        try {
//            PharmacyDTO pharmacyDTO = objectMapper.readValue(pharmacyJSONString, PharmacyDTO.class);
//            return dtoToPharmacy(pharmacyDTO);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
