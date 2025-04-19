package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findById(Long id);

    List<Report> findAll();

    @Query("select r from Report r join Hospital h where h.id = :hospitalId")
    List<Report> getReportByHospitalId(Long hospitalId);

    @Query("select r from Report r join Pharmacy p where p.id = :pharmacyId")
    List<Report> getReportByPharmacyId(Long pharmacyId);

    @Query("select r from Report r join Member m where m.id = :memberId")
    List<Report> getReportByMemberId(Long memberId);

    Report save(Report report);

    void removeReportById(Long id);


}
