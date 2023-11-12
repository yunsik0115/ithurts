package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {
    public List<Pharmacy> findByName(String name);

    public List<Pharmacy> findByAddressContains(String address);

    public List<Pharmacy> findAll();

    Pharmacy findPharmacyByNameAndAddress(String name, String address);
}
