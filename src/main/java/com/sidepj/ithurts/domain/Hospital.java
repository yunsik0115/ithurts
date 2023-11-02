package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hospital {

    @Id
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
}