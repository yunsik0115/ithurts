package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pharmacy {

    @Id
    @Column(name = "pharmacy_id")
    private int id;

    private String name;

    private String contact;

    private String Address; //Embedded Class Address will be made - TO DO -

    private String officeDay;

    private String officeTime;

    private Point coordinates;

    private LocalDateTime updatedOn;
    private LocalDateTime createdOn;

    private boolean is_available;

    @OneToMany(mappedBy = "pharmacy")
    private List<Report> reports = new ArrayList<>();


}
