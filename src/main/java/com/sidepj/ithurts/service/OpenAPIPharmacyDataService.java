package com.sidepj.ithurts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.repository.HospitalRepository;
import com.sidepj.ithurts.repository.PharmacyRepository;
import com.sidepj.ithurts.service.dto.PharmacyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:API-KEY.yml")
public class OpenAPIPharmacyDataService {

    private final HospitalRepository hospitalRepository;
    private final PharmacyRepository pharmacyRepository;
    private final ObjectMapper objectMapper;

    @Value("${OPENAPI-Pharmacy-SecretKey}") // Lombok의 Value가 아님
    private String serviceKeyValue;

    public boolean retrieveDataByCityName(String cityName) throws IOException {
        log.trace("====== Start Retrieving Pharmacy Data By cityName From OPENAPI ======");

        // == TO-DO Method Extraction Code Refactoring == //

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire"); /*URL*/
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", "UTF-8")).append("=").append(serviceKeyValue); /*Service Key*/
        urlBuilder.append("&").append(URLEncoder.encode("Q0", "UTF-8")).append("=").append(URLEncoder.encode(cityName, "UTF-8")); /*주소(시도)*/
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
        JSONArray pharmacies = xmlJSONObj.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
        JsonMapper jsonMapper = JsonMapper.builder().build();
        List<PharmacyDTO> pharmacyDTOList = new ArrayList<>();

        log.trace("===========PharmacyDTOs Creation============");
        for(int i = 0; i < pharmacies.length(); i++){
            JSONObject pharmacy = pharmacies.getJSONObject(i);
            String pharmacyJSONString = pharmacy.toString();
            PharmacyDTO pharmacyDTO = jsonMapper.readValue(pharmacyJSONString, PharmacyDTO.class);
            pharmacyDTOList.add(pharmacyDTO);
            log.trace("{}", pharmacyDTO);
        }


        return true;
    }

    public List<Pharmacy> dtoToPharmacy(List<PharmacyDTO> pharmacyDTOS){
        Pharmacy pharmacy = new Pharmacy();

        for (PharmacyDTO pharmacyDTO : pharmacyDTOS) {
            pharmacy.setName(pharmacyDTO.getDutyName());
            pharmacy.setContact(pharmacyDTO.getDutyTel1());
            pharmacy.setAddress(pharmacyDTO.getDutyAddr());
        }
        //pharmacy.setOfficeDay();

        return new ArrayList<>();
    }

    public Map<String, LocalTime> officeDayExtractionFromDTOs(PharmacyDTO pharmacyDTO){

        Map<String, LocalTime> officeDay = new HashMap<>();

        //c - 오전  s- 오후
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        LocalTime mondayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
        officeDay.put("monOPEN", mondayOPEN);
        LocalTime mondayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime1s(), formatter);
        officeDay.put("mondCLOSED", mondayCLOSED);
        LocalTime tuesdayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
        officeDay.put("tueOPEN", tuesdayOPEN);
        LocalTime tuesdayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
        officeDay.put("tueCLOSED", tuesdayCLOSED);
        LocalTime wednesdayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
        officeDay.put("wedOPEN", wednesdayOPEN);
        LocalTime wednesdayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
        officeDay.put("wedCLOSED", wednesdayCLOSED);
        LocalTime thursdayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
        officeDay.put("thuOPEN", thursdayOPEN);
        LocalTime thursdayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
        officeDay.put("thuCLOSED", thursdayCLOSED);
        LocalTime fridayOPEN = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
        officeDay.put("friOPEN", fridayOPEN);
        LocalTime fridayCLOSED = LocalTime.parse(pharmacyDTO.getDutyTime1c(), formatter);
        officeDay.put("friCLOSED", fridayCLOSED);

        return officeDay;
    }

}
