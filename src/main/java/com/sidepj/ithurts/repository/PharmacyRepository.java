package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.domain.Pharmacy;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    @Query("SELECT p FROM Pharmacy p WHERE FUNCTION('ST_DISTANCE_SPHERE', p.coordinates, :point) < :distance ")
    public Optional<List<Pharmacy>> findByRadius(@Param("point") Point point , @Param("distance") double distance);

    @Query("SELECT p FROM Pharmacy p WHERE FUNCTION('ST_DISTANCE_SPHERE', p.coordinates, :point) < :distance ")
    public Page<Pharmacy> findByRadius(@Param("point") Point point , @Param("distance") double distance, Pageable pageable);


    @Query("SELECT p FROM Pharmacy p JOIN p.officeTimes ot " +
            "WHERE FUNCTION('ST_DISTANCE_SPHERE', p.coordinates, :point) < :distance " +
            "AND ot.weekday = :weekday " +
            "AND ot.startOffice <= :currentTime AND ot.endOffice >= :currentTime")
    List<Pharmacy> findOpenPharmaciesNearLocation(
            @Param("point") Point point,
            @Param("distance") double distance,
            @Param("weekday") String weekday,
            @Param("currentTime") LocalTime currentTime
    );

    @Query("SELECT p FROM Pharmacy p JOIN p.officeTimes ot " +
            "WHERE FUNCTION('ST_DISTANCE_SPHERE', p.coordinates, :point) < :distance " +
            "AND ot.weekday = :weekday " +
            "AND ot.startOffice <= :currentTime AND ot.endOffice >= :currentTime")
    Page<Pharmacy> findOpenPharmaciesNearLocation(
            @Param("point") Point point,
            @Param("distance") double distance,
            @Param("weekday") String weekday,
            @Param("currentTime") LocalTime currentTime,
            Pageable pageable
    );

    public List<Pharmacy> findByName(String name);
    public Page<Pharmacy> findByName(String name, Pageable pageable);
    public List<Pharmacy> findByAddressContains(String address);
    public Page<Pharmacy> findByAddressContains(String address, Pageable pageable);


    public List<Pharmacy> findAll();
    public Page<Pharmacy> findAll(Pageable pageable);

    Pharmacy findPharmacyByNameAndAddress(String name, String address);
    Page<Pharmacy> findPharmacyByNameAndAddress(String name, String address, Pageable pageable);

    public void removePharmacyById(Long id);
}
