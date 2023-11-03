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


    private int pharmId; // FK for which pharmacy's data is wrong.

    private int hospId; // FK for which hospital's data is wrong.

    private LocalDateTime createdAt;

    private String comment; // reason which will be entered by users.


}
