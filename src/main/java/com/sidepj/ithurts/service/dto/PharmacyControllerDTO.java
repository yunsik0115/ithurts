package com.sidepj.ithurts.service.dto;

import com.sidepj.ithurts.domain.Pharmacy;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
public class PharmacyControllerDTO {

    private Long id;

    private String name;

    private String contact;

    private String address;

    private Double x_cord;

    private Double y_cord;

    private LocalDateTime updatedDate;

    private LocalDateTime createdDate;


    public PharmacyControllerDTO() {
    }
}
