package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="reports")
public class Report {

    @Id
    @Column(name = "report_id")
    private int id;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member report_member;// FK for user who created this report.


    @ManyToOne
    @JoinColumn(name = "pharmacy_id")
    private Pharmacy pharmacy;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital; // FK for which hospital's data is wrong.

    @Column(name = "report_created_date")
    private LocalDateTime createdDate;

    @Column(name = "report_comment")
    private String comment; // reason which will be entered by users.


}
