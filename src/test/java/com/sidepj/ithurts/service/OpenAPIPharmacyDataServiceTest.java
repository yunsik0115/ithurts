package com.sidepj.ithurts.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class OpenAPIPharmacyDataServiceTest {

    @Autowired
    OpenAPIPharmacyDataService op;

    @Test
    void openAPITest() throws JsonProcessingException {
        try {
            op.retrieveDataByCityName("서울특별시");
            op.retrieveDataByCityName("구리시");
            op.retrieveDataByCityName("안양시");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}