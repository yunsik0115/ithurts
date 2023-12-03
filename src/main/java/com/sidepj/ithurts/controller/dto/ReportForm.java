package com.sidepj.ithurts.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReportForm {

    private String name;

    private String content;

    private Long targetId;

    private String reportType; // enum? hospital vs pharmacy


}
