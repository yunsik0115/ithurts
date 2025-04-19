package com.sidepj.ithurts.controller.REST.jsonDTO;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.LoveJSON;
import com.sidepj.ithurts.domain.Love;

import java.util.List;

public class LoveJsonResponse {

    private int status;

    private String message;

    private List<LoveJSON> loves;

    public LoveJsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public LoveJsonResponse(int status, String message, List<LoveJSON> loves) {
        this.status = status;
        this.message = message;
        this.loves = loves;
    }

}
