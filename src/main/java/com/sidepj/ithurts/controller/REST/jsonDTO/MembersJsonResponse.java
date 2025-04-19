package com.sidepj.ithurts.controller.REST.jsonDTO;

import com.sidepj.ithurts.controller.REST.jsonDTO.data.UserJSON;
import com.sidepj.ithurts.domain.Member;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MembersJsonResponse {

    private int status;

    private String message;

    private List<UserJSON> members;

    public MembersJsonResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.members = null;
    }

    public MembersJsonResponse(int status, String message, List<UserJSON> members) {
        this.status = status;
        this.message = message;
        this.members = members;
    }
}
