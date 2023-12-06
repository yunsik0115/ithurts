package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.domain.Pharmacy;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    @Query("SELECT p FROM Pharmacy p WHERE FUNCTION('ST_DISTANCE_SPHERE', p.coordinates, :point) < :distance ")
    public List<Pharmacy> findByRadius(@Param("point") Point point , @Param("distance") double distance);
    public List<Pharmacy> findByName(String name);
    public List<Pharmacy> findByAddressContains(String address);

    public List<Pharmacy> findAll();

    Pharmacy findPharmacyByNameAndAddress(String name, String address);
}
