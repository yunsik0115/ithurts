package com.sidepj.ithurts.domain;

import lombok.*;
import org.springframework.data.geo.Point;

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

    @Column(name = "hospital_coordinates")
    private Point coordinates;

    @Column(name = "hospital_updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "hospital_created_date")
    private LocalDateTime createdDate;


    // private boolean is_available; 서비스단에서 처리

    @OneToMany(mappedBy = "hospital")
    private List<Report> reports = new ArrayList<>();

    public void addTime(HospitalOfficeTime hospitalOfficeTime){
        this.officeTimes.add(hospitalOfficeTime);
        hospitalOfficeTime.setHospital(this);
    }
}