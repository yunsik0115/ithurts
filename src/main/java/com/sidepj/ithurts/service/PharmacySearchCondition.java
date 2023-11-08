package com.sidepj.ithurts.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PharmacySearchCondition {
    private String Q0; // 주소 (시도)
    private String Q1; // 주소 (시군구)

    private String QN; // 기관명
    private Integer QT; // 진료요일

    private Integer ORD; // 순서

    private Integer pageNo; // 페이지 번호

    private Integer numOfRows; // 받아올 건수

}
