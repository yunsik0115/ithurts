package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    private int id;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User report_user;// FK for user who created this report.


    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital; // FK for which hospital's data is wrong.

    private LocalDateTime createdAt;

    private String comment; // reason which will be entered by users.


}
