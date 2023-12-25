package com.sidepj.ithurts.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="reports")
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne @JsonIgnore
    @JoinColumn(name = "member_id")
    private Member report_member;// FK for user who created this report.


    private Long pharmHospId;

    private String reportType;

    private Boolean isChecked;

    @Column(name = "report_created_date")
    private LocalDateTime createdDate;

    @Column(name = "report_comment")
    private String comment; // reason which will be entered by users.

    private LocalDateTime modifiedAt;

    public Report() {
        this.isChecked = false;
    }
}
