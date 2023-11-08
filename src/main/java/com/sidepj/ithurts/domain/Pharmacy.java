package com.sidepj.ithurts.domain;

import lombok.*;
import org.springframework.data.geo.Point;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pharmacy_id")
    private int id;

    @Column(name = "pharmacy_name")
    private String name;

    @Column(name = "pharmacy_contact")
    private String contact;

    @Column(name="pharmacy_address")
    private String address; //Embedded Class Address will be made - TO DO -

    @Column(name = "pharmacy_coordinates")
    private Point coordinates;

    @Column(name = "pharmacy_updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "pharmacy_created_date")
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "pharmacy")
    private List<OfficeTime> officeTime = new ArrayList<>();

    // private boolean is_available; 서비스 단에서 처리

    @OneToMany(mappedBy = "pharmacy")
    private List<Report> reports = new ArrayList<>();

    public void addTime(OfficeTime officeTime){
        this.officeTime.add(officeTime);
        officeTime.setPharmacy(this);

    }


}
