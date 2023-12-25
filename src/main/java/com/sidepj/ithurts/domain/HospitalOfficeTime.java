package com.sidepj.ithurts.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"hospital"})
public class HospitalOfficeTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne @JsonIgnore
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

    public void setHospital(Hospital hospital){
        this.hospital = hospital;
        hospital.getOfficeTimes().add(this);
    }
}
