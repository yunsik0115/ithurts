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

import java.util.List;

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
        List<Pharmacy> pharmacies = pharmacyDataService.searchByName("365동서약국");
        for (Pharmacy pharmacy : pharmacies) {
            System.out.println("pharmacy = " + pharmacy.toString());
        }
    }

    @Test
    void searchByCity() {
        List<Pharmacy> pharmacies = pharmacyDataService.searchByCity("서울시");
        for (Pharmacy pharmacy : pharmacies) {
            System.out.println("pharmacy = " + pharmacy.toString());
        }
    }

    @Test
    void searchByDetailedCity() {
        List<Pharmacy> pharmacies = pharmacyDataService.searchByDetailedCity("서울시", "광진구");
        for (Pharmacy pharmacy : pharmacies) {
            System.out.println("pharmacy = " + pharmacy.toString());
        }

    }
}