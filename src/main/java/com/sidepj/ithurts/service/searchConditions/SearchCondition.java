package com.sidepj.ithurts.service.searchConditions;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchCondition {

    private String city; // 주소 (시도)
    private String detailedCity; // 주소 (시군구)

    private String servicePart; // 진료과목

    private String officeName; // 기관명
    private Integer officeDay; // 진료요일

    private Integer order; // 순서

    private Integer pageNo; // 페이지 번호

    private Integer numOfRows; // 받아올 건수

    private Boolean searchAll;

}
