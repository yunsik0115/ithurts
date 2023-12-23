package com.sidepj.ithurts.controller.REST.jsonDTO;

import com.sidepj.ithurts.service.naverapiservice.NaverMapAPISearchResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.URL;

@Data
@AllArgsConstructor
public class NaverMapAPISearchResponse {

    private NaverMapAPISearchResult nvr;
    private URL url;

}
