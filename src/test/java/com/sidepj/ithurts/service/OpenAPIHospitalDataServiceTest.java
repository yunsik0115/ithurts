package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIHospitalDataService;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@SpringBootTest
@Transactional
class OpenAPIHospitalDataServiceTest {

    @Autowired
    OpenAPIHospitalDataService openAPIHospitalDataService;

    @Autowired
    DataService<Hospital> hospitalService;

    @PersistenceContext
    EntityManager em;

    @Test
    void test() throws IOException {
        SearchCondition searchCondition = SearchCondition.builder()
                .city("서울특별시")
                .detailedCity("광진구")
                .build();
        List<Hospital> hospitals = openAPIHospitalDataService.retrieve(searchCondition);

        Assertions.assertThat(hospitals).isNotEmpty();
    }

    @Test
    @Transactional
    @Commit
    void saveAllinDatabase(){
        SearchCondition searchCondition = SearchCondition.builder().searchAll(true).build();
        List<Hospital> retrieve = openAPIHospitalDataService.retrieve(searchCondition);
        for (Hospital hospital : retrieve) {
            em.persist(hospital);
        }
        em.flush();
        em.clear();
        List<Hospital> all = hospitalService.getAll();
        Assertions.assertThat(retrieve).hasSize(all.size());
    }

}