package com.sidepj.ithurts.controller.REST.jsonDTO.data;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class PharmacyJSON {

    private Long pharmacyId;

    private String name;

    private LocalTime monOpen;
    private LocalTime monClosed;

    private LocalTime tueOpen;
    private LocalTime tueClosed;

    private LocalTime wedOpen;
    private LocalTime wedClosed;

    private LocalTime thuOpen;
    private LocalTime thuClosed;

    private LocalTime friOpen;
    private LocalTime friClosed;

    private LocalTime satSunOpen;
    private LocalTime satSunClosed;

    private LocalTime holidayOpen;
    private LocalTime holidayClosed;

    private LocalDateTime updateDate;
    private LocalDateTime createdDate;

    private double coord_x;
    private double coord_y;

}
