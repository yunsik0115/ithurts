package com.sidepj.ithurts.controller.REST.jsonDTO;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.PharmacyJSON;
import com.sidepj.ithurts.domain.Pharmacy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PharmacyJsonResponse {

    // Pharmacy와 Hospital의 JSON 형식을 담음

    private int status;

    private String message;

    private List<PharmacyJSON> pharmacyList;


    public PharmacyJsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.pharmacyList = new ArrayList<>();
    }

    public PharmacyJsonResponse(int status, String message, List<PharmacyJSON> pharmaciesJSON) {
        this.status = status;
        this.message = message;
        this.pharmacyList = pharmaciesJSON;
    }

    public PharmacyJsonResponse(int status, String message, PharmacyJSON pharmacyJSON) {
        this.status = status;
        this.message = message;
        this.pharmacyList = new ArrayList<>();
        this.pharmacyList.add(pharmacyJSON);
    }

    private PharmacyJsonResponse createEmpty(final int statusCode, final String responseMessage){
        return createJsonWithEntities(statusCode, responseMessage, null);
    }

    private PharmacyJsonResponse createJsonWithEntities(final int statusCode, final String responseMessage, List<PharmacyJSON> pharmacyJSONList){
        return PharmacyJsonResponse.builder()
                .pharmacyList(pharmacyJSONList)
                .status(statusCode)
                .message(responseMessage)
                .build();
    }

    private PharmacyJsonResponse createJsonWithEntity(final int statusCode, final String responseMessage, PharmacyJSON pharmacyJSON){
        PharmacyJsonResponse created = PharmacyJsonResponse.builder()
                .status(statusCode)
                .message(responseMessage)
                .build();
        created.getPharmacyList().add(pharmacyJSON);
        return created;
    }
}
