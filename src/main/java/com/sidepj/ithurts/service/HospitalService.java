package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.repository.HospitalRepository;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIDataService;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService implements DataService<HospitalControllerDTO> {

    private final HospitalRepository hospitalRepository;
    private final OpenAPIDataService<Hospital> openAPIHospitalDataService;



    @Override
    public List<HospitalControllerDTO> searchByName(String officeName) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByName(officeName);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().officeName(officeName).build();
            // DB에 Persist는 이미 완료된 상태
            return transferToDTOS(openAPIHospitalDataService.retrieve(sc));
        }
        return transferToDTOS(retrievedFromDB);
    }

    @Override
    public List<HospitalControllerDTO> searchByCity(String cityName) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByAddressContains(cityName, "");
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().city(cityName).build();
            return transferToDTOS(openAPIHospitalDataService.retrieve(sc));
            // DB에 Persist는 이미 완료된 상태
        }
        return transferToDTOS(retrievedFromDB);
    }

    @Override
    public List<HospitalControllerDTO> searchByDetailedCity(String cityName, String detailedCity) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByAddressContains(cityName, detailedCity);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().city(cityName).detailedCity(detailedCity).build();
            return transferToDTOS(openAPIHospitalDataService.retrieve(sc));

        }
        return transferToDTOS(retrievedFromDB);
    }

    @Override
    public List<HospitalControllerDTO> searchByServiceType(String serviceType) {
        List<Hospital> retrievedFromDB = hospitalRepository.findByHospitalType(serviceType);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().servicePart(serviceType).build();
            return transferToDTOS(openAPIHospitalDataService.retrieve(sc));
            // DB에 Persist는 이미 완료된 상태

        }
        return transferToDTOS(retrievedFromDB);
    }

    @Override
    public List<HospitalControllerDTO> searchBySearchCondition(SearchCondition searchCondition) {
        return null;
    }

    @Override
    public List<HospitalControllerDTO> getAll() {
        return transferToDTOS(hospitalRepository.findAll());
    }

    @Override
    public HospitalControllerDTO findById(Long id) {
        Optional<Hospital> findHospital = hospitalRepository.findById(id);
        if(findHospital.isPresent()){
            Hospital hospital = findHospital.get();
            return entityDtoTransferValidation(hospital);
        } else{
            throw new IllegalArgumentException("해당 병원 정보를 가져올 수 없습니다");
        }
    }

    @Override
    public List<HospitalControllerDTO> retrieveAll() {
        SearchCondition searchCondition = SearchCondition.builder().city("서울특별시").detailedCity("광진구").searchAll(true).build();
        return transferToDTOS(openAPIHospitalDataService.retrieve(searchCondition));
    }

    public List<HospitalControllerDTO> transferToDTOS(List<Hospital> hospitalList){
        List<HospitalControllerDTO> list = new ArrayList<>();
        for (Hospital hospital : hospitalList) {
            list.add(entityDtoTransferValidation(hospital));
        }
        return list;
    }

    public HospitalControllerDTO entityDtoTransferValidation(Hospital entity){
        HospitalControllerDTO dto = new HospitalControllerDTO();
        if(entity.getId() != null){

            dto.setId(entity.getId());
        }

        if(entity.getName() != null){
            dto.setName(entity.getName());
        }

        if(entity.getContact() != null){
            dto.setContact(entity.getContact());
        }

        if(entity.getAddress() != null){
            dto.setAddress(entity.getAddress());
        }

        if(entity.getCoordinates() != null){
            dto.setX_cord(entity.getCoordinates().getX());
            dto.setY_cord(entity.getCoordinates().getY());
        }

        if(entity.getCreatedDate() != null){
            dto.setCreatedDate(entity.getCreatedDate());
        }

        if(entity.getUpdatedDate() != null){
            dto.setUpdatedDate(entity.getUpdatedDate());
        }

        return dto;
    }
}
