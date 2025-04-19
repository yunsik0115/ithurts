package com.sidepj.ithurts.service.naverapiservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class NaverMapAPISearchResult {

    private String title;

    private String sid;

}
