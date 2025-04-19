package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.repository.PharmacyOfficeTimeRepository;
import com.sidepj.ithurts.repository.PharmacyRepository;
import com.sidepj.ithurts.service.dto.jsonparsingdto.HospitalDTO;
import com.sidepj.ithurts.service.dto.jsonparsingdto.PharmacyDTO;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIDataService;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    public List<Pharmacy> searchOpened(double longitude, double latitude, double radius, boolean holiday) {
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
        return pharmacyRepository.findOpenPharmaciesNearLocation(point, radius, weekday, currentTime);
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
    public void remove(Long id) {
        pharmacyRepository.removePharmacyById(id);
    }


    @Override
    public List<Pharmacy> getAll() {
        return pharmacyRepository.findAll();
    }

    @Override
    public List<Pharmacy> retrieveAll() {
        SearchCondition searchCondition = SearchCondition.builder().city("서울특별시").detailedCity("광진구").searchAll(true).build();
        return openAPIPharmacyDataService.retrieve(searchCondition);
    }

    @Override
    public Page<Pharmacy> searchByName(String officeName, Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();


        Page<Pharmacy> retrievedFromDB = pharmacyRepository.findByName(officeName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().officeName(officeName).build();
            List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태
            return pharmacyRepository.findByName(officeName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
        }
        return retrievedFromDB;
    }

    @Override
    public Page<Pharmacy> searchByCity(String cityName, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();
        PageRequest.of(page, size, Sort.by(Sort.Direction.DESC));

        Page<Pharmacy> retrievedFromDB = pharmacyRepository.findByAddressContains(cityName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().city(cityName).build();
            List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태
            return pharmacyRepository.findByAddressContains(cityName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
        }
        return retrievedFromDB;
    }

    @Override // 코드 제거 (코드가 searchByCity와 동일)
    public Page<Pharmacy> searchByDetailedCity(String cityName, String detailedCity, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        Page<Pharmacy> retrievedFromDB = pharmacyRepository.findByAddressContains(cityName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().city(cityName).build();
            List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태
            return pharmacyRepository.findByAddressContains(cityName, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
        }
        return retrievedFromDB;
    }

    @Override
    public Page<Pharmacy> searchByCoordiateAndRadius(double longitude, double latitude, double radius, Pageable pageable) {

        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();



        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);
        Page<Pharmacy> pharmaciesByRadius = pharmacyRepository.findByRadius(point, radius, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
        if(pharmaciesByRadius.isEmpty()){
            throw new IllegalStateException("지역 근방에 약국이 없습니다, 검색 범위를 변경해보세요!");
        }
        return pharmaciesByRadius;
    }


    @Override
    public Page<Pharmacy> getAll(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        return pharmacyRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    @Override
    public Page<Pharmacy> searchOpened(double longitude, double latitude, double radius, boolean holiday, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        int size = pageable.getPageSize();

        LocalDateTime now = LocalDateTime.now();
        String weekday = now.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        LocalTime currentTime = now.toLocalTime();

        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Point point = geometryFactory.createPoint(coordinate);

        return pharmacyRepository.findOpenPharmaciesNearLocation(point, radius, weekday, currentTime, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC)));
    }

    @Override
    public void updateEntityFromDTO(Long id, Object instance) {
        // DTO가 PharmacyDTO인지 확인
        if (!(instance instanceof PharmacyDTO)) {
            throw new IllegalArgumentException("잘못된 접근입니다");
        }

        PharmacyDTO dto = (PharmacyDTO) instance;

        // 엔티티를 ID로 찾음
        Pharmacy entity = findById(id);

        // 약국 이름이 null이 아니거나 공백이 아닌 경우에만 업데이트
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
