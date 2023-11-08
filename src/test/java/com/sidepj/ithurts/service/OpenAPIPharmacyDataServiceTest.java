package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIPharmacyDataService;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@Transactional
class OpenAPIPharmacyDataServiceTest {

    @Autowired
    OpenAPIPharmacyDataService pharmacyDataService;

    @Test
    void openAPITest() throws IOException {

        SearchCondition pharmacySearchCondition = SearchCondition.builder()
                        .city("서울특별시").detailedCity("광진구").build();

        List<Pharmacy> retrieve = pharmacyDataService.retrieve(pharmacySearchCondition);

        Assertions.assertThat(retrieve).isNotEmpty();
    }

}