package com.sidepj.ithurts.service.jsonparsingservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.domain.HospitalOfficeTime;
import com.sidepj.ithurts.repository.HospitalRepository;
import com.sidepj.ithurts.repository.HospitalOfficeTimeRepository;
import com.sidepj.ithurts.service.dto.jsonparsingdto.HospitalDTO;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
@Slf4j
@PropertySource("classpath:API-KEY.yml")
@RequiredArgsConstructor
public class OpenAPIHospitalDataService implements OpenAPIDataService<Hospital> {

    private final HospitalRepository hospitalRepository;
    private final HospitalOfficeTimeRepository hospitalOfficeTimeRepository;

    private final ObjectMapper objectMapper;

    @Value("${OPENAPI-Hospital-SecretKey}") // Lombok의 Value가 아님
    private String serviceKeyValue;

    // TO - DO DTO Transfering logic HERE //

    public List<Hospital> retrieve(SearchCondition searchCondition){
        log.trace("====== Start Retrieving Hospital Datas From OPENAPI ======");

        JSONObject xmlJSONObj = null;
        try {
            xmlJSONObj = getJsonObject(searchCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.trace("xmljsonobj = {}", xmlJSONObj);

        JSONArray hospitals;
        Object itemObj = xmlJSONObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").get("item");

        // JSON 객체와 배열 모두 처리
        if (itemObj instanceof JSONArray) {
            hospitals = (JSONArray) itemObj;
        } else if (itemObj instanceof JSONObject) {
            hospitals = new JSONArray();
            hospitals.put((JSONObject) itemObj);
        } else {
            throw new JSONException("item key is neither a JSONObject nor a JSONArray");
        }

        JsonMapper jsonMapper = JsonMapper.builder().build();
        List<HospitalDTO> hospitalDTOList = new ArrayList<>();

        log.trace("===========Hospital DTOs Creation============");
        for(int i = 0; i < hospitals.length(); i++){
            JSONObject hospital = hospitals.getJSONObject(i);
            String hospitalJSONString = hospital.toString();
            HospitalDTO hospitalDTO = null;
            try {
                hospitalDTO = jsonMapper.readValue(hospitalJSONString, HospitalDTO.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            hospitalDTOList.add(hospitalDTO);
            log.trace("{}", hospitalDTO);
        }

        log.trace("============hospital DTO Entity Transformation Completed ============");


        return dtosToHospital(hospitalDTOList);
    }

//    @Override
//    public Hospital retrieveOne(SearchCondition searchCondition){
//        log.trace("====== Start Retrieving Hospital Datas From OPENAPI ======");
//
//        JSONObject xmlJSONObj = null;
//        try {
//            xmlJSONObj = getJsonObject(searchCondition);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        log.trace("xmljsonobj = {}", xmlJSONObj);
//        try {
//            JSONObject hospital = xmlJSONObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONObject("item");
//            JsonMapper jsonMapper = JsonMapper.builder().build();
//            String hospitalJSONString = hospital.toString();
//            HospitalDTO hospitalDTO = objectMapper.readValue(hospitalJSONString, HospitalDTO.class);
//            return dtoToHospital(hospitalDTO);
//        } catch (JSONException e){
//            throw new IllegalArgumentException("검색 결과가 없습니다");
//        } catch (JsonMappingException e) {
//            throw new RuntimeException(e);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private JSONObject getJsonObject(SearchCondition searchCondition) throws IOException {
        StringBuilder urlBuilder = getUrlBySearchCondition(searchCondition);
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
        log.trace("==========JSON STRING FROM OPENAPI============");
        log.trace("{}", xmlJSONObj.toString());
        return xmlJSONObj;
    }

    private StringBuilder getUrlBySearchCondition(SearchCondition searchCondition) throws UnsupportedEncodingException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/HsptlAsembySearchService/getHsptlMdcncListInfoInqire"); /*URL*/
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", "UTF-8")).append("=").append(serviceKeyValue); /*Service Key*/

        /*주소(시도)*/
        if(StringUtils.hasText(searchCondition.getCity())){ // 주소 (시도)
            urlBuilder.append("&").append(URLEncoder.encode("Q0", "UTF-8")).append("=").append(URLEncoder.encode(searchCondition.getCity(), "UTF-8"));
        }

        if(StringUtils.hasText(searchCondition.getDetailedCity())){ // 주소 (시군구)
            urlBuilder.append("&").append(URLEncoder.encode("Q1", "UTF-8")).append("=").append(URLEncoder.encode(searchCondition.getDetailedCity(), "UTF-8"));
        }

        if(StringUtils.hasText(searchCondition.getServicePart())){ // 주소 (시군구)
            urlBuilder.append("&").append(URLEncoder.encode("QZ", "UTF-8")).append("=").append(URLEncoder.encode(searchCondition.getServicePart(), "UTF-8"));
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

    private List<Hospital> dtosToHospital(List<HospitalDTO> hospitalDTOS){
        log.trace("=================DTO TO Hospital TRANSFERRATION ==============");

        List<Hospital> hospitalTransferedhospitalList = new ArrayList<>();
        for (HospitalDTO hospitalDTO : hospitalDTOS) {

            Hospital hospital = new Hospital(); // 객체 생성은 반복할때마다 생성하지 않으면 Dirty Checking에 의해 기존 Row가 update 됨.
            hospital.setId(null);
            hospital.setName(hospitalDTO.getDutyName());
            hospital.setContact(hospitalDTO.getDutyTel1());
            hospital.setAddress(hospitalDTO.getDutyAddr());
            if(hospitalDTO.getWgs84Lat() != null && hospitalDTO.getWgs84Lon() != null) {
                hospital.setCoordinates(new Point(hospitalDTO.getWgs84Lat(), hospitalDTO.getWgs84Lon()));
            }
            Hospital savedOne = hospitalRepository.save(hospital);
            officeTimeInjectionFromDTOs(hospitalDTO, savedOne);
            log.trace("transfered.pharm = {}", savedOne);
            hospitalTransferedhospitalList.add(savedOne);
        }

        return hospitalTransferedhospitalList;
    }

    private void officeTimeInjectionFromDTOs(HospitalDTO hospitalDTO, Hospital hospital){

        List<HospitalOfficeTime> HospitalOfficeTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm", Locale.KOREA);
        //c - 오후  s- 오전
        HospitalOfficeTime mon = new HospitalOfficeTime("monday", null, null, hospital);
        if(hospitalDTO.getDutyTime1s() != null || hospitalDTO.getDutyTime1c() != null) {
            LocalTime mondayOPEN = LocalTime.parse(hospitalDTO.getDutyTime1s(), formatter);
            LocalTime mondayCLOSED = LocalTime.parse(hospitalDTO.getDutyTime1c(), formatter);
            mon = new HospitalOfficeTime("monday", mondayOPEN, mondayCLOSED, hospital);
        }
        hospitalOfficeTimeRepository.save(mon);
        hospital.addTime(mon);

        HospitalOfficeTime tue = new HospitalOfficeTime("tuesday", null, null, hospital);
        if(hospitalDTO.getDutyTime2c() != null && hospitalDTO.getDutyTime2s() != null) {
            LocalTime tuesdayOPEN = LocalTime.parse(hospitalDTO.getDutyTime2s(), formatter);
            LocalTime tuesdayCLOSED = LocalTime.parse(hospitalDTO.getDutyTime2c(), formatter);
            tue = new HospitalOfficeTime("tuesday", tuesdayOPEN, tuesdayCLOSED, hospital);
        }
        hospitalOfficeTimeRepository.save(tue);
        hospital.addTime(tue);

        HospitalOfficeTime wed = new HospitalOfficeTime("wednesday", null, null, hospital);
        if(hospitalDTO.getDutyTime3s() != null && hospitalDTO.getDutyTime3c() != null) {
            LocalTime wednesdayOPEN = LocalTime.parse(hospitalDTO.getDutyTime3s(), formatter);
            LocalTime wednesdayCLOSED = LocalTime.parse(hospitalDTO.getDutyTime3c(), formatter);
            wed = new HospitalOfficeTime("wednesday", wednesdayOPEN, wednesdayCLOSED, hospital);
        }
        hospitalOfficeTimeRepository.save(wed);
        hospital.addTime(wed);

        HospitalOfficeTime thu = new HospitalOfficeTime("thursday", null, null, hospital);
        if(hospitalDTO.getDutyTime4s() != null && hospitalDTO.getDutyTime4c() != null) {
            LocalTime thursdayOPEN = LocalTime.parse(hospitalDTO.getDutyTime4s(), formatter);
            LocalTime thursdayCLOSED = LocalTime.parse(hospitalDTO.getDutyTime4c(), formatter);
            thu = new HospitalOfficeTime("thursday", thursdayOPEN, thursdayCLOSED, hospital);
        }
        hospitalOfficeTimeRepository.save(thu);
        hospital.addTime(thu);

        HospitalOfficeTime fri = new HospitalOfficeTime("thursday", null, null, hospital);
        if(hospitalDTO.getDutyTime5s() != null && hospitalDTO.getDutyTime5c() != null) {
            LocalTime fridayOPEN = LocalTime.parse(hospitalDTO.getDutyTime5s(), formatter);
            LocalTime fridayCLOSED = LocalTime.parse(hospitalDTO.getDutyTime5c(), formatter);
            fri = new HospitalOfficeTime("friday", fridayOPEN, fridayCLOSED, hospital);
        }
        hospitalOfficeTimeRepository.save(fri);
        hospital.addTime(fri);

        HospitalOfficeTime satSun = new HospitalOfficeTime("satsun", null, null, hospital);
        if(hospitalDTO.getDutyTime7s() != null && hospitalDTO.getDutyTime7c() != null) {
            LocalTime satSunOpen = LocalTime.parse(hospitalDTO.getDutyTime7s(), formatter);
            LocalTime satSunClosed = LocalTime.parse(hospitalDTO.getDutyTime7c(), formatter);
            satSun = new HospitalOfficeTime("satsun", satSunOpen, satSunClosed, hospital);
        }
        hospitalOfficeTimeRepository.save(satSun);
        hospital.addTime(satSun);

        HospitalOfficeTime holiday = new HospitalOfficeTime("holiday", null, null, hospital);
        if(hospitalDTO.getDutyTime8s() != null && hospitalDTO.getDutyTime8c() != null) {
            LocalTime holidayOpen = LocalTime.parse(hospitalDTO.getDutyTime8s(), formatter);
            LocalTime holidayClosed = LocalTime.parse(hospitalDTO.getDutyTime8c(), formatter);
            holiday = new HospitalOfficeTime("holiday", holidayOpen, holidayClosed, hospital);
        }
        hospitalOfficeTimeRepository.save(holiday);
        hospital.addTime(holiday);

    }

}