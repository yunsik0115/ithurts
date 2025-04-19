package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.repository.HospitalRepository;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.dto.jsonparsingdto.HospitalDTO;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIDataService;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
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
    public List<Hospital> searchOpened(double longitude, double latitude, double radius, boolean holiday) {
        // 현재 시간과 요일을 자동으로 계산
        LocalDateTime now = LocalDateTime.now();
        String weekday = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        LocalTime currentTime = now.toLocalTime();

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);

        if(holiday){
            weekday = "holiday";
        }

        // 리포지토리 메서드 호출
        return hospitalRepository.findOpenHospitalsNearLocation(point, radius, weekday, currentTime);
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


    public void remove(Long id){
        hospitalRepository.removeHospitalById(id);
    }


    @Override
    public List<Hospital> getAll() {
        return hospitalRepository.findAll();
    }

    @Override
    public Hospital findById(Long id) {
        Optional<Hospital> findHospital = hospitalRepository.findById(id);
        if(findHospital.isPresent()){
            return findHospital.get();
        } else{
            throw new IllegalArgumentException("해당 병원 정보를 가져올 수 없습니다");
        }
    }

    @Override
    public List<Hospital> retrieveAll() {
        SearchCondition searchCondition = SearchCondition.builder().city("서울특별시").detailedCity("광진구").searchAll(true).build();
        return openAPIHospitalDataService.retrieve(searchCondition);
    }

    @Override
    public Page<Hospital> searchByName(String officeName, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        return hospitalRepository.findByNameLike(officeName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    @Override
    public Page<Hospital> searchByCity(String cityName, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();
        return hospitalRepository.findByAddressContains(cityName, cityName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    @Override
    public Page<Hospital> searchByDetailedCity(String cityName, String detailedCity, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        return hospitalRepository.findByAddressContains(cityName, cityName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    @Override
    public Page<Hospital> searchByCoordiateAndRadius(double longitude, double latitude, double radius, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();


        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);

        LocalDateTime now = LocalDateTime.now();
        String weekday = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        LocalTime currentTime = now.toLocalTime();
        return hospitalRepository.findOpenHospitalsNearLocation(point, radius, weekday, currentTime, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }


    @Override
    public Page<Hospital> getAll(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();
        Pageable pageRequest = PageRequest.of(page, size);

        return hospitalRepository.findAll(pageRequest);
    }


    @Override
    public Page<Hospital> searchOpened(double longitude, double latitude, double radius, boolean holiday, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();


        // 현재 시간과 요일을 자동으로 계산
        LocalDateTime now = LocalDateTime.now();
        String weekday = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        LocalTime currentTime = now.toLocalTime();

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);

        if(holiday){
            weekday = "holiday";
        }

        // 리포지토리 메서드 호출
        return hospitalRepository.findOpenHospitalsNearLocation(point, radius, weekday, currentTime, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    public void updateEntityFromDTO(Long id, Object instance) {

        if(!(instance instanceof HospitalDTO)){
            throw new IllegalArgumentException("잘못된 접근입니다");
        }

        HospitalDTO dto = (HospitalDTO) instance;

        Hospital entity = findById(id);

        // 병원 이름이 null이 아니거나 공백이 아닌 경우에만 업데이트
        if (dto.getDutyName() != null && !dto.getDutyName().trim().isEmpty()) {
            entity.setName(dto.getDutyName());
        }

        // 주소가 null이 아니거나 공백이 아닌 경우에만 업데이트
        if (dto.getDutyAddr() != null && !dto.getDutyAddr().trim().isEmpty()) {
            entity.setAddress(dto.getDutyAddr());
        }

        // 전화번호가 null이 아니거나 공백이 아닌 경우에만 업데이트
        if (dto.getDutyTel1() != null && !dto.getDutyTel1().trim().isEmpty()) {
            entity.setContact(dto.getDutyTel1());
        }

        // 병원 분류가 null이 아니거나 공백이 아닌 경우에만 업데이트
        if (dto.getDutyDivNam() != null && !dto.getDutyDivNam().trim().isEmpty()) {
            entity.setHospitalType(dto.getDutyDivNam());
        }

        // 좌표 정보가 null이 아닌 경우에만 업데이트 (경도 및 위도)
        if (dto.getWgs84Lat() != null && dto.getWgs84Lon() != null) {
            GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
            Coordinate coordinate = new Coordinate(dto.getWgs84Lon(), dto.getWgs84Lat());
            Point point = geometryFactory.createPoint(coordinate);
            entity.setCoordinates(point);
        }

        // 업데이트 시간 설정
        entity.setUpdatedDate(LocalDateTime.now());
    }



}
