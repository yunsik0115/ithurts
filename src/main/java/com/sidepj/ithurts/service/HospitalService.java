package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.repository.HospitalRepository;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIDataService;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService implements DataService<Hospital> {

    private final HospitalRepository hospitalRepository;
    private final OpenAPIDataService<Hospital> openAPIHospitalDataService;

    @Override
    public List<Hospital> searchByName(String officeName) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByName(officeName);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().officeName(officeName).build();
            // DB에 Persist는 이미 완료된 상태
            return openAPIHospitalDataService.retrieve(sc);
        }
        return retrievedFromDB;
    }

    @Override
    public List<Hospital> searchByCity(String cityName) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByAddressContains(cityName, "");
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().city(cityName).build();
            List<Hospital> retrieve = openAPIHospitalDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태
            return retrieve;
        }
        return retrievedFromDB;
    }

    @Override
    public List<Hospital> searchByDetailedCity(String cityName, String detailedCity) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByAddressContains(cityName, detailedCity);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().city(cityName).detailedCity(detailedCity).build();
            List<Hospital> retrieve = openAPIHospitalDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태
            return retrieve;
        }
        return retrievedFromDB;
    }

    @Override
    public List<Hospital> searchByServiceType(String serviceType) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByHospitalType(serviceType);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().servicePart(serviceType).build();
            List<Hospital> retrieve = openAPIHospitalDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태
            return retrieve;
        }
        return retrievedFromDB;
    }

    @Override
    public List<Hospital> searchBySearchCondition(SearchCondition searchCondition) {
        return null;
    }
}
