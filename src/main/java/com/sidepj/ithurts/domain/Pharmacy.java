package com.sidepj.ithurts.domain;

import com.sidepj.ithurts.domain.converter.OfficeTimeToJsonConverter;
import lombok.*;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @Lob
    @Column(name= "pharmacy_officetime")
    @Convert(converter = OfficeTimeToJsonConverter.class)
    private Map<String, LocalTime> officeTime = new HashMap<>();

    @Column(name = "pharmacy_coordinates")
    private Point coordinates;

    @Column(name = "pharmacy_updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "pharmacy_created_date")
    private LocalDateTime createdDate;

    public Pharmacy(String name, String contact, String address, Map<String, LocalTime> officeTime, Point coordinates) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.officeTime = officeTime;
        this.coordinates = coordinates;
    }

    // private boolean is_available; 서비스 단에서 처리

    @OneToMany(mappedBy = "pharmacy")
    private List<Report> reports = new ArrayList<>();


}
