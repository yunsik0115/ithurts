package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

}
