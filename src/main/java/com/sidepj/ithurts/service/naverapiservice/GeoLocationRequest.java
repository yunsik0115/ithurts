package com.sidepj.ithurts.service.naverapiservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class GeoLocationRequest {

    private String ip;

    private String ext;

    private String responseFormatType;
}
