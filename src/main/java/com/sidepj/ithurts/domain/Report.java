package com.sidepj.ithurts.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    private int id;

    private int userId; // FK for user who created this report.

    private int pharmId; // FK for which pharmacy's data is wrong.

    private int hospId; // FK for which hospital's data is wrong.

    private LocalDateTime createdAt;

    private String comment; // reason which will be entered by users.


}
