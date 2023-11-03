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
    private int id;

    private String name;

    private String contact;

    private String Address; //Embedded Class Address will be made - TO DO -

    private String hospitalType;

    private String officeDay;

    private String officeTime;

    private Point coordinates;

    private LocalDateTime updatedOn;
    private LocalDateTime createdOn;

    private boolean is_available;

    @OneToMany(mappedBy = "hospital")
    private List<Report> reports = new ArrayList<>();
}