package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Hospital;
import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    public List<Hospital> findByName(String officeName);


    //public List<Hospital> findByNameWithRadius(String officeName, Point coordinates);

    /*
    Spherical Law of Cosines Formula
    (37 and -122 are the latitude and longitude of your radius center)


    SELECT id, ( 3959 * acos( cos( radians(37) ) * cos( radians( lat ) )
    * cos( radians( long ) - radians(-122) ) + sin( radians(37) ) * sin(radians(lat)) ) ) AS distance
    FROM myTable
    HAVING distance < 50
    ORDER BY distance
     */

    @Query("select h from Hospital h where h.address LIKE '%:searchTarget%' || '%:searchTarget2%' ")
    public List<Hospital> findByAddressContains(String searchTarget,String searchTarget2);

    public List<Hospital> findByHospitalType(String hospitalType);

    public List<Hospital> findAll();

    public Hospital findHospitalByNameAndAddressAndHospitalType(String name, String address, String hospitalType);

}
