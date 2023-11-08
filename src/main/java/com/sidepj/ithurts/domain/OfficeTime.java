package com.sidepj.ithurts.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
public class OfficeTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Pharmacy pharmacy;

    private String weekday;

    private LocalTime startOffice;

    private LocalTime endOffice;

    public OfficeTime(String weekday, LocalTime startOffice, LocalTime endOffice, Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        this.weekday = weekday;
        this.startOffice = startOffice;
        this.endOffice = endOffice;
    }
}
