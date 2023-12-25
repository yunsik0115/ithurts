package com.sidepj.ithurts.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String content;

    @NotBlank
    private Long targetId;

    private String targetName;

    private String reportType; // enum? hospital vs pharmacy

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

}
