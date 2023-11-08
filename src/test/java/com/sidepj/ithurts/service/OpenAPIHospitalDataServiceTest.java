package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
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
class OpenAPIHospitalDataServiceTest {

    @Autowired
    OpenAPIHospitalDataService openAPIHospitalDataService;

    @Test
    void test() throws IOException {
        SearchCondition searchCondition = SearchCondition.builder()
                .city("서울특별시")
                .detailedCity("광진구")
                .build();
        List<Hospital> hospitals = openAPIHospitalDataService.retrieve(searchCondition);

        Assertions.assertThat(hospitals).isNotEmpty();
    }

}