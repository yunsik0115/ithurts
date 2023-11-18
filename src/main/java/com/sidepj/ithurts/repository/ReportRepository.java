package com.sidepj.ithurts.repository;

import com.sidepj.ithurts.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Long, Report> {

    Report getReportById(Long id);
    List<Report> getReports();

    @Query("select r from Report r join Hospital h where h.id = :hospitalId")
    List<Report> getReportByHospitalId(Long hospitalId);

    @Query("select r from Report r join Pharmacy p where p.id = :pharmacyId")
    List<Report> getReportByPharmacyId(Long pharmacyId);

    @Query("select r from Report r join Member m where m.id = :memberId")
    List<Report> getReportByMemberId(Long memberId);

    Report save(Report report);

    void removeById(Long id);


}
