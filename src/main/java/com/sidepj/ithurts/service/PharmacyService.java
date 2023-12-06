package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.repository.PharmacyOfficeTimeRepository;
import com.sidepj.ithurts.repository.PharmacyRepository;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIDataService;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PharmacyService implements DataService<Pharmacy> {

    private final PharmacyRepository pharmacyRepository;
    private final PharmacyOfficeTimeRepository pharmacyOfficeTimeRepository;
    private final OpenAPIDataService<Pharmacy> openAPIPharmacyDataService;


    @Override
    public List<Pharmacy> searchByCoordinateAndRadius(double longitude, double latitude, double radius) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);
        Optional<List<Pharmacy>> pharmaciesByRadius = pharmacyRepository.findByRadius(point, radius);
        if(pharmaciesByRadius.isEmpty()){
            throw new IllegalStateException("지역 근방에 약국이 없습니다, 검색 범위를 변경해보세요!");
        }
        return pharmaciesByRadius.get();
    }

    @Override
    public List<Pharmacy> searchByName(String officeName) {
        List<Pharmacy> retrievedFromDB = pharmacyRepository.findByName(officeName);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().officeName(officeName).build();
            List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태
            return retrieve;
        }
        return retrievedFromDB;
    }

    @Override
    public List<Pharmacy> searchByCity(String cityName) { // ~~시를 통해 시 내에 있는 모든 데이터 검색 시
        List<Pharmacy> retrievedFromDB = pharmacyRepository.findByAddressContains(cityName);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().city(cityName).build();
            List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(sc);
            return retrieve;
        }
        return retrievedFromDB;
    }

    @Override
    public List<Pharmacy> searchByDetailedCity(String cityName, String detailedCity) {
        List<Pharmacy> retrievedFromDB = pharmacyRepository.findByAddressContains(detailedCity);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().detailedCity(detailedCity).build();
            List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(sc);
            return retrieve;
        }
        return retrievedFromDB;
    }

    @Override
    public Pharmacy findById(Long id) {
        Optional<Pharmacy> optionalPharmacy = pharmacyRepository.findById(id);
        if(optionalPharmacy.isPresent()){
            Pharmacy pharmacy = optionalPharmacy.get();
            return pharmacy;
        } else{
            throw new IllegalArgumentException("해당 약국 정보를 가져올 수 없습니다");
        }
    }

    @Override
    public List<Pharmacy> searchByServiceType(String serviceType) {
        return new ArrayList<>();
    }

    @Override
    public List<Pharmacy> searchBySearchCondition(SearchCondition searchCondition) {
        return null;
    }

    @Override
    public List<Pharmacy> getAll() {
        return pharmacyRepository.findAll();
    }

    @Override
    public List<Pharmacy> retrieveAll() {
        List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(SearchCondition.builder().searchAll(true).build());
        return retrieve;
    }

}
