package com.sidepj.ithurts.controller.REST.jsonDTO.data;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserJSON {

    private Long id;

    private String name;

    private LocalDateTime createdDate;

    private LocalDateTime lastPwdChanged;

    private String role;


}
