package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hospitals")
public class Hospital {

    @Id
    @Column(name="hospital_id")
    private Long id;

    @Column(name="hospital_name")
    private String name;

    @Column(name="hospital_contact")
    private String contact;

    @Column(name="hospital_address")
    private String Address; // To-Do Region으로 나누기

    @Column(name="hospital_type")
    private String hospitalType;

    @Column(name="hospital_officeday")
    private String officeDay;

    @Column(name = "hospital_officetime")
    private String officeTime;

    @Column(name = "hospital_coordinates")
    private Point coordinates;

    @Column(name = "hospital_updated_date")
    private LocalDateTime updatedOn;
    @Column(name = "hospital_created_date")
    private LocalDateTime createdOn;


    // private boolean is_available; 서비스단에서 처리

    @OneToMany(mappedBy = "hospital")
    private List<Report> reports = new ArrayList<>();
}