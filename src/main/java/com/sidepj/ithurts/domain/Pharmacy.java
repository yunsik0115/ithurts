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
@Table(name = "pharmacy")
public class Pharmacy {

    @Id
    @Column(name = "pharmacy_id")
    private int id;

    @Column(name = "pharmacy_name")
    private String name;

    @Column(name = "pharmacy_contact")
    private String contact;

    @Column(name="pharmacy_address")
    private String Address; //Embedded Class Address will be made - TO DO -

    @Column(name = "pharmacy_officeday")
    private String officeDay;

    @Column(name= "pharmacy_officetime")
    private String officeTime;

    @Column(name = "pharmacy_coordinates")
    private Point coordinates;

    @Column(name = "pharmacy_updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "pharmacy_created_date")
    private LocalDateTime createdDate;

    // private boolean is_available; 서비스 단에서 처리

    @OneToMany(mappedBy = "pharmacy")
    private List<Report> reports = new ArrayList<>();


}
