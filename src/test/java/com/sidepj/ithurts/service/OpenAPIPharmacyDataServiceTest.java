package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Pharmacy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class OpenAPIPharmacyDataServiceTest {

    @Autowired
    OpenAPIPharmacyDataService pharmacyDataService;

    @Test
    void openAPITest() throws IOException {

        PharmacySearchCondition pharmacySearchCondition = PharmacySearchCondition.builder()
                        .city("서울특별시").detailedCity("광진구").build();

        List<Pharmacy> retrieve = pharmacyDataService.retrieve(pharmacySearchCondition);

        Assertions.assertThat(retrieve).isNotEmpty();
    }

}