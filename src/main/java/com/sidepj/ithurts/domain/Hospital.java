package com.sidepj.ithurts.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@ToString
@Getter @Setter
@Table(name = "hospitals")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="hospital_id")
    private Long id;

    @Column(name="hospital_name")
    private String name;

    @Column(name="hospital_contact")
    private String contact;

    @Column(name="hospital_address")
    private String address; // To-Do Region으로 나누기

    @Column(name="hospital_type")
    private String hospitalType;

    @OneToMany(mappedBy = "hospital")
    private List<HospitalOfficeTime> officeTimes = new ArrayList<>();

    @Column(name = "hospital_coordinates", columnDefinition = "GEOMETRY")
    private Point coordinates;

    @Column(name = "hospital_updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "hospital_created_date")
    private LocalDateTime createdDate;


    // private boolean is_available; 서비스단에서 처리

//    @OneToMany(mappedBy = "hospital") 특정 병원에 대한 Report 정보를 매핑할 필요가 있을까?
//    private List<Report> reports = new ArrayList<>();

    public void addTime(HospitalOfficeTime hospitalOfficeTime){
        this.officeTimes.add(hospitalOfficeTime);
        hospitalOfficeTime.setHospital(this);
    }
}