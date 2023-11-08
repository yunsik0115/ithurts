package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    public List<Hospital> findByName(String officeName);

    @Query("select h from Hospital h where h.address LIKE '%:searchTarget%' || '%:searchTarget2%' ")
    public List<Hospital> findByAddressContains(String searchTarget,String searchTarget2);

    public List<Hospital> findByHospitalType(String hospitalType);


}
