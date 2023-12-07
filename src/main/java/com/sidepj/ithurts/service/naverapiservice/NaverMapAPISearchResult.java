package com.sidepj.ithurts.service.naverapiservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class NaverMapAPISearchResult {

    private String id;

    private String title;

    private String sid;


}
