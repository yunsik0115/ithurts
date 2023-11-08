package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.repository.HospitalRepository;
import com.sidepj.ithurts.repository.PharmacyOfficeTimeRepository;
import com.sidepj.ithurts.repository.PharmacyRepository;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PharmacyServiceTest {

    private DataService<Pharmacy> pharmacyDataService;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private OpenAPIDataService<Pharmacy> openAPIDataService;

    @Autowired
    private PharmacyOfficeTimeRepository pharmacyOfficeTimeRepository;

    @BeforeEach
    public void beforeEach(){
        pharmacyDataService = new PharmacyService(pharmacyRepository, pharmacyOfficeTimeRepository ,openAPIDataService);
    }

    @Test
    void searchByName() {
        //pharmacyDataService.searchByName("365동서약국");
    }

    @Test
    void searchByCity() {
    }

    @Test
    void searchByDetailedCity() {
    }
}