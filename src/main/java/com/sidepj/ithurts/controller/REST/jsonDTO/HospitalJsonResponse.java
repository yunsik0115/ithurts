package com.sidepj.ithurts.controller.REST.jsonDTO;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.HospitalJSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class HospitalJsonResponse {

    // Pharmacy와 Hospital의 JSON 형식을 담음

    private int status;

    private String message;

    private List<HospitalJSON> hospitalList;


    public HospitalJsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.hospitalList = new ArrayList<>();
    }

    public HospitalJsonResponse(int status, String message, List<HospitalJSON> hospitalJSONS) {
        this.status = status;
        this.message = message;
        this.hospitalList = hospitalJSONS;
    }

    public HospitalJsonResponse(int status, String message, HospitalJSON hospital) {
        this.status = status;
        this.message = message;
        this.hospitalList = new ArrayList<>();
        this.hospitalList.add(hospital);
    }

    private static HospitalJsonResponse createEmpty(final int statusCode, final String responseMessage){
        return createJsonWithEntities(statusCode, responseMessage, null);
    }

    private static HospitalJsonResponse createJsonWithEntities(final int statusCode, final String responseMessage, List<HospitalJSON> hospitalJsonList){
        return HospitalJsonResponse.builder()
                .hospitalList(hospitalJsonList)
                .status(statusCode)
                .message(responseMessage)
                .build();
    }

    private static HospitalJsonResponse createJsonWithEntity(final int statusCode, final String responseMessage, HospitalJSON hospitalJson){
        HospitalJsonResponse created = HospitalJsonResponse.builder()
                .status(statusCode)
                .message(responseMessage)
                .build();
        created.getHospitalList().add(hospitalJson);
        return created;
    }
}
