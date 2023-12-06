package com.sidepj.ithurts.service.naverapiservice;

import lombok.Data;

@Data
public class GeoLocation {

    private String code;

    private String r1; // 도 광역시 주

    private String r2; // 시 군 구

    private String r3; // 동 면 읍

    private Double lat; // 위도

    private Double lon; // 경도

    private String isp; // 통신사
}
