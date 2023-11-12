package com.sidepj.ithurts.service;

import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.repository.PharmacyOfficeTimeRepository;
import com.sidepj.ithurts.repository.PharmacyRepository;
import com.sidepj.ithurts.service.dto.PharmacyControllerDTO;
import com.sidepj.ithurts.service.jsonparsingservice.OpenAPIDataService;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PharmacyService implements DataService<PharmacyControllerDTO> {

    private final PharmacyRepository pharmacyRepository;
    private final PharmacyOfficeTimeRepository pharmacyOfficeTimeRepository;
    private final OpenAPIDataService<Pharmacy> openAPIPharmacyDataService;

    @Override
    public List<PharmacyControllerDTO> searchByName(String officeName) {
        List<Pharmacy> retrievedFromDB = pharmacyRepository.findByName(officeName);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().officeName(officeName).build();
            List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(sc);
            // DB에 Persist는 이미 완료된 상태
            return transferToDTO(retrieve);
        }
        return transferToDTO(retrievedFromDB);
    }

    @Override
    public List<PharmacyControllerDTO> searchByCity(String cityName) { // ~~시를 통해 시 내에 있는 모든 데이터 검색 시
        List<Pharmacy> retrievedFromDB = pharmacyRepository.findByAddressContains(cityName);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().city(cityName).build();
            List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(sc);
            List<PharmacyControllerDTO> pharmacyControllerDTOS = transferToDTO(retrieve);
            return pharmacyControllerDTOS;
        }
        return transferToDTO(retrievedFromDB);
    }

    @Override
    public List<PharmacyControllerDTO> searchByDetailedCity(String cityName, String detailedCity) {
        List<Pharmacy> retrievedFromDB = pharmacyRepository.findByAddressContains(detailedCity);
        if(retrievedFromDB.isEmpty()){
            SearchCondition sc = SearchCondition.builder().detailedCity(detailedCity).build();
            List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(sc);
            return transferToDTO(retrieve);
        }
        return transferToDTO(retrievedFromDB);
    }

    @Override
    public List<PharmacyControllerDTO> searchByServiceType(String serviceType) {
        return new ArrayList<>();
    }

    @Override
    public List<PharmacyControllerDTO> searchBySearchCondition(SearchCondition searchCondition) {
        return null;
    }

    @Override
    public List<PharmacyControllerDTO> getAll() {
        return transferToDTO(pharmacyRepository.findAll());
    }

    @Override
    public List<PharmacyControllerDTO> retrieveAll() {
        List<Pharmacy> retrieve = openAPIPharmacyDataService.retrieve(SearchCondition.builder().searchAll(true).build());
        return transferToDTO(retrieve);
    }

    public List<PharmacyControllerDTO> transferToDTO(List<Pharmacy> pharmacyList){
        List<PharmacyControllerDTO> list = new ArrayList<>();
        for (Pharmacy pharmacy : pharmacyList) {
            list.add(entityDtoTransferValidation(pharmacy));
        }
        return list;
    }

    public PharmacyControllerDTO entityDtoTransferValidation(Pharmacy entity){
        PharmacyControllerDTO dto = new PharmacyControllerDTO();
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
            dto.setCreatedDate(LocalDateTime.now());
        }

        dto.setUpdatedDate(LocalDateTime.now());

        return dto;
    }
}
