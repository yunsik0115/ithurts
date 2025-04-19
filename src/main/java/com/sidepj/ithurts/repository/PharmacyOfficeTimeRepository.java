package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.PharmacyOfficeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PharmacyOfficeTimeRepository extends JpaRepository<PharmacyOfficeTime, Long> {
}
