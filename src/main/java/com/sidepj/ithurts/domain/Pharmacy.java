package com.sidepj.ithurts.domain;

import lombok.*;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
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

    public Pharmacy(String name, String contact, String address, String officeDay, String officeTime, Point coordinates) {
        this.name = name;
        this.contact = contact;
        Address = address;
        this.officeDay = officeDay;
        this.officeTime = officeTime;
        this.coordinates = coordinates;
    }

    // private boolean is_available; 서비스 단에서 처리

    @OneToMany(mappedBy = "pharmacy")
    private List<Report> reports = new ArrayList<>();


}
