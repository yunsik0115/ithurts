package com.sidepj.ithurts.domain;

import lombok.*;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    private String address; //Embedded Class Address will be made - TO DO -

    // OfficeDay - JSON으로 넣을것인가 / Column을 14개(공휴일 포함) 만들것인가?
    @Column(name = "pharmacy_officeday")
    private String officeDay;

    @Type(type = "json")
    @Column(name= "pharmacy_officetime", columnDefinition = "longtext")
    private Map<String, Object> officeTime;

    @Column(name = "pharmacy_coordinates")
    private Point coordinates;

    @Column(name = "pharmacy_updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "pharmacy_created_date")
    private LocalDateTime createdDate;

    public Pharmacy(String name, String contact, String address, String officeDay, Map<String, Object> officeTime, Point coordinates) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.officeDay = officeDay;
        this.officeTime = officeTime;
        this.coordinates = coordinates;
    }

    // private boolean is_available; 서비스 단에서 처리

    @OneToMany(mappedBy = "pharmacy")
    private List<Report> reports = new ArrayList<>();


}
