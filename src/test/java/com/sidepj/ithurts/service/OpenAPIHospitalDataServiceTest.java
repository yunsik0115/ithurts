package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class OpenAPIHospitalDataServiceTest {

    @Autowired
    OpenAPIHospitalDataService openAPIHospitalDataService;

    @Test
    void test() throws IOException {
        HospitalSearchCondition hospitalSearchCondition = HospitalSearchCondition.builder()
                .city("서울특별시")
                .detailedCity("광진구")
                .build();
        List<Hospital> hospitals = openAPIHospitalDataService.retrieveAll(hospitalSearchCondition);

        Assertions.assertThat(hospitals).isNotEmpty();
    }

}