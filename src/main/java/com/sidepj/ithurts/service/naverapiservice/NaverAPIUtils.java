package com.sidepj.ithurts.service.naverapiservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@Transactional
@RequiredArgsConstructor
public class NaverAPIUtils {

    private final NaverAPIService naverService;

    private final String testIP = "210.125.183.15";

    public GeoLocationResponse getLocation(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        ResponseEntity<GeoLocationResponse> geoLocationResponseEntity = naverService.geoLocationRetrieve(request, testIP);
        if(!geoLocationResponseEntity.getStatusCode().equals(HttpStatus.OK)){
            throw new IllegalStateException("서버에서 위치 정보를 가져오지 못했습니다");
        }
        return geoLocationResponseEntity.getBody();
    }

    public NaverMapAPISearchResult getSearchResult(String searchName, Double lon, Double lat) throws JsonProcessingException {
        return naverService.getSearchResult(searchName, lon, lat);
    }



}
