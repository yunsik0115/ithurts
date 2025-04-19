package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Hospital;

import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    public List<Hospital> findByName(String officeName);

    public Page<Hospital> findByNameLike(String name, Pageable pageable);


    @Query("SELECT h FROM Hospital h WHERE FUNCTION('ST_DISTANCE_SPHERE', h.coordinates, :point) < :distance ")
    public Optional<List<Hospital>> findByRadius(@Param("point") Point point , @Param("distance") double distance);

    @Query("SELECT h FROM Hospital h WHERE FUNCTION('ST_DISTANCE_SPHERE', h.coordinates, :point) < :distance ")
    public Page<Hospital> findByRadius(@Param("point") Point point , @Param("distance") double distance, Pageable pageable);

    @Query("SELECT h FROM Hospital h JOIN h.officeTimes ot " +
            "WHERE FUNCTION('ST_DISTANCE_SPHERE', h.coordinates, :point) < :distance " +
            "AND ot.weekday = :weekday " +
            "AND ot.startOffice <= :currentTime AND ot.endOffice >= :currentTime")
    List<Hospital> findOpenHospitalsNearLocation(
            @Param("point") Point point,
            @Param("distance") double distance,
            @Param("weekday") String weekday,
            @Param("currentTime") LocalTime currentTime
    );

    @Query("SELECT h FROM Hospital h JOIN h.officeTimes ot " +
            "WHERE FUNCTION('ST_DISTANCE_SPHERE', h.coordinates, :point) < :distance " +
            "AND ot.weekday = :weekday " +
            "AND ot.startOffice <= :currentTime AND ot.endOffice >= :currentTime")
    Page<Hospital> findOpenHospitalsNearLocation(
            @Param("point") Point point,
            @Param("distance") double distance,
            @Param("weekday") String weekday,
            @Param("currentTime") LocalTime currentTime,
            Pageable pageable
    );

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

    @Query("select h from Hospital h where h.address LIKE '%:searchTarget%' || '%:searchTarget2%' ")
    public Page<Hospital> findByAddressContains(String searchTarget,String searchTarget2, Pageable pageable);

    public List<Hospital> findByHospitalType(String hospitalType);

    public Page<Hospital> findByHospitalType(String hospitalType, Pageable pageable);

    public List<Hospital> findAll();

    public Page<Hospital> findAll(Pageable pageable);

    // 1개 이상 나오면 안됨
    public Hospital findHospitalByNameAndAddressAndHospitalType(String name, String address, String hospitalType);

    public void removeHospitalById(Long id);
}
