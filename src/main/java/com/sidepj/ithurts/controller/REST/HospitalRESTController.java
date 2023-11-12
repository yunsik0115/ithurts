package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.service.DataService;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import com.sidepj.ithurts.service.searchConditions.SearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/maps/hospitals")
public class HospitalRESTController {

    @Autowired
    DataService<HospitalControllerDTO> hospitalService;

    @PostMapping("/retrieve")
    public ResponseEntity<List<HospitalControllerDTO>> retrieveAll(@ModelAttribute SearchCondition searchCondition){
        List<HospitalControllerDTO> allDTOs = hospitalService.retrieveAll();
        return new ResponseEntity<>(allDTOs, HttpStatus.OK);
    }

    @GetMapping("/retrieve")
    public ResponseEntity<List<HospitalControllerDTO>> retrieve(@ModelAttribute SearchCondition searchCondition){
        List<HospitalControllerDTO> result = hospitalService.searchByDetailedCity(searchCondition.getCity(), searchCondition.getDetailedCity());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
