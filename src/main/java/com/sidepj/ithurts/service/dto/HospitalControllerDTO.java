package com.sidepj.ithurts.service.dto;

import com.sidepj.ithurts.domain.Hospital;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HospitalControllerDTO {

    private Long id;

    private String name;

    private String contact;

    private String hospitalType;

    private String address;

    private Double x_cord;
    private Double y_cord;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public HospitalControllerDTO(){

    }


}
