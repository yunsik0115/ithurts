package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.repository.HospitalRepository;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.dto.jsonparsingdto.HospitalDTO;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class HospitalServiceTest {

    private HospitalService hospitalDataService;
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private OpenAPIDataService<Hospital> openAPIDataService;

    @BeforeEach
    public void beforeEach(){
        hospitalDataService = new HospitalService(hospitalRepository, openAPIDataService);
    }

    @Test
    void searchByName() {
        List<HospitalControllerDTO> hospital = hospitalDataService.searchByName("대한산업보건협회군산산업보건센타군산서해의원");
        System.out.println("hospital.toString() = " + hospital.toString());
    }

    @Test
    void searchByCity() {
        List<HospitalControllerDTO> hospitals = hospitalDataService.searchByCity("서울특별시");
        for (HospitalControllerDTO hospital : hospitals) {
            System.out.println("hospital.toString() = " + hospital.toString());
        }
    }

    @Test
    void searchByDetailedCity() {
        List<HospitalControllerDTO> hospitals = hospitalDataService.searchByDetailedCity("서울특별시", "광진구");
        for (HospitalControllerDTO hospital : hospitals) {
            System.out.println("hospital.toString() = " + hospital.toString());
        }
    }

    @Test
    void searchByServiceType() {
    }
}