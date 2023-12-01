package com.sidepj.ithurts.controller.REST.jsonDTO;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.PharmacyJSON;
import com.sidepj.ithurts.controller.REST.jsonDTO.data.UserJSON;
import com.sidepj.ithurts.domain.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MemberJsonResponse {

    private int status;

    private String message;

    private UserJSON member;

    public MemberJsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.member = null;
    }

    public MemberJsonResponse(int status, String message, UserJSON member) {
        this.status = status;
        this.message = message;
        this.member = member;
    }
}
