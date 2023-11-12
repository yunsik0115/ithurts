package com.sidepj.ithurts.controller.REST;

import com.sidepj.ithurts.service.DataService;
import com.sidepj.ithurts.service.dto.HospitalControllerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController("/map/hospitals")
public class HospitalRESTController {

    @Autowired
    DataService<HospitalControllerDTO> hospitalService;

    @PostMapping("/retrieve")
    public ResponseEntity<List<HospitalControllerDTO>> retrieveAll(){
        List<HospitalControllerDTO> allDTOs = hospitalService.retrieveAll();
        return new ResponseEntity<>(allDTOs, HttpStatus.OK);
    }

    @GetMapping("/retrieve")
    public ResponseEntity<List<HospitalControllerDTO>> retrieve(){
        return new ResponseEntity<>(hospitalService.getAll(), HttpStatus.OK);
    }


}
