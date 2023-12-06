package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.repository.HospitalRepository;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.dto.jsonparsingdto.HospitalDTO;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIDataService;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService implements DataService<Hospital> {

    private final HospitalRepository hospitalRepository;
    private final OpenAPIDataService<Hospital> openAPIHospitalDataService;

    @Override
    public List<Hospital> searchByCoordinateAndRadius(double longitude, double latitude, double radius) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);
        Optional<List<Hospital>> hospitalsByRadius = hospitalRepository.findByRadius(point, radius);
        if(hospitalsByRadius.isEmpty()){
            throw new IllegalStateException("근방 내에 병원이 없습니다. 검색 범위를 변경해보세요!");
        }
        return hospitalsByRadius.get();
    }

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
            return openAPIHospitalDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태
        }
        return retrievedFromDB;
    }

    @Override
    public List<Hospital> searchByDetailedCity(String cityName, String detailedCity) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByAddressContains(cityName, detailedCity);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().city(cityName).detailedCity(detailedCity).build();
            return openAPIHospitalDataService.retrieve(sc);

        }
        return retrievedFromDB;
    }

    @Override
    public List<Hospital> searchByServiceType(String serviceType) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByHospitalType(serviceType);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().servicePart(serviceType).build();
            return openAPIHospitalDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태

        }
        return retrievedFromDB;
    }

    @Override
    public List<Hospital> searchBySearchCondition(SearchCondition searchCondition) {
        return null;
    }

    @Override
    public List<Hospital> getAll() {
        return hospitalRepository.findAll();
    }

    @Override
    public Hospital findById(Long id) {
        Optional<Hospital> findHospital = hospitalRepository.findById(id);
        if(findHospital.isPresent()){
            Hospital hospital = findHospital.get();
            return hospital;
        } else{
            throw new IllegalArgumentException("해당 병원 정보를 가져올 수 없습니다");
        }
    }

    @Override
    public List<Hospital> retrieveAll() {
        SearchCondition searchCondition = SearchCondition.builder().city("서울특별시").detailedCity("광진구").searchAll(true).build();
        return openAPIHospitalDataService.retrieve(searchCondition);
    }

}
