package com.sidepj.ithurts.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
public class HospitalOfficeTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Hospital hospital;

    private String weekday;

    private LocalTime startOffice;

    private LocalTime endOffice;

    public HospitalOfficeTime(String weekday, LocalTime startOffice, LocalTime endOffice, Hospital hospital) {
        this.hospital = hospital;
        this.weekday = weekday;
        this.startOffice = startOffice;
        this.endOffice = endOffice;
    }
}
