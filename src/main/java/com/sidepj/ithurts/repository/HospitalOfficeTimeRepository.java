package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.HospitalOfficeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalOfficeTimeRepository extends JpaRepository<HospitalOfficeTime, Long> {
}
