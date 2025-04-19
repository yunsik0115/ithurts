package com.sidepj.ithurts.service.dto.jsonparsingdto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class PharmacyDTO {

    // API Parsing용 DTO (DTO -> Entity 변환 메서드 - OpenAPIPharmacyDataService 참고

    private String dutyAddr; // 주소
    private String dutyName; // 약국 이름
    private String dutyTel1; // 대표 전화 1

    // c: 오전 s: 오후 - OPENAPI Spec
    private String dutyTime1c;
    private String dutyTime1s;
    private String dutyTime2c;
    private String dutyTime2s;
    private String dutyTime3c;
    private String dutyTime3s;
    private String dutyTime4c;
    private String dutyTime4s;
    private String dutyTime5c;
    private String dutyTime5s;
    private String dutyTime6c;
    private String dutyTime6s;

    private String dutyTime7c; // Sunday
    private String dutyTime7s;

    private String dutyTime8s; // 공휴일
    private String dutyTime8c;

    private Double wgs84Lon; // 경도
    private Double wgs84Lat;

}
