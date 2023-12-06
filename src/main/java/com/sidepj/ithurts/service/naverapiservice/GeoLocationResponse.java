package com.sidepj.ithurts.service.naverapiservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString
public class GeoLocationResponse {

    private String requestId;

    private String returnCode;

    private GeoLocation geoLocation;


}
