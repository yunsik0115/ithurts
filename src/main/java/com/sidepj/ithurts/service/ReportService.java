package com.sidepj.ithurts.service;

import com.sidepj.ithurts.controller.dto.ReportDTO;
import com.sidepj.ithurts.domain.Hospital;
import com.sidepj.ithurts.domain.Member;
import com.sidepj.ithurts.domain.Pharmacy;
import com.sidepj.ithurts.domain.Report;
import com.sidepj.ithurts.repository.HospitalRepository;
import com.sidepj.ithurts.repository.MemberRepository;
import com.sidepj.ithurts.repository.PharmacyRepository;
import com.sidepj.ithurts.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService {

    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final PharmacyRepository pharmacyRepository;
    private final HospitalRepository hospitalRepository;

    public void save(ReportDTO reportDTO, Long memberId, Long targetId) throws IllegalAccessException {

        Optional<Member> byId = memberRepository.findById(memberId);

        if(byId.isEmpty()){
            throw new IllegalAccessException("회원 정보를 찾을 수 없습니다, 로그인 상태를 확인하세요");
        }

        Report report = new Report();
        report.setReport_member(byId.get());
        report.setName(reportDTO.getName());
        report.setPharmHospId(reportDTO.getPharmHospId());
        report.setReportType(reportDTO.getReportType());
        report.setComment(reportDTO.getContent());
        report.setCreatedDate(LocalDateTime.now());

        Optional<Pharmacy> pharmacy = pharmacyRepository.findById(targetId);
        Optional<Hospital> hospital = hospitalRepository.findById(targetId);

        if(pharmacy.isPresent()){
            report.setPharmHospId(targetId);
        }

        if(hospital.isPresent()){
            report.setPharmHospId(targetId);
        }

        if((pharmacy.isPresent() && hospital.isPresent()) || (hospital.isEmpty()) && pharmacy.isEmpty()){
            throw new IllegalArgumentException("DB상에 문제가 조회되었습니다, 관리자에게 문의해주세요.");
        }

        reportRepository.save(report);
    }

    public List<Report> getReports(){
        List<Report> all = reportRepository.findAll();
        return all;
    }

    public Report getReport(Long id){
        Optional<Report> report = reportRepository.findById(id);
        if(report.isPresent()){
            return report.get();
        }

        throw new NoSuchElementException("해당 정보를 찾을 수 없습니다");
    }

    public Report modifyReport(Long id, ReportDTO reportDTO){
        Optional<Report> byId = reportRepository.findById(id);

        if(byId.isEmpty()){
            throw new NoSuchElementException("해당 신고 정보를 찾을 수 없습니다");
        }

        Report report = byId.get();
        report.setName(reportDTO.getName());
        report.setComment(reportDTO.getContent());
        report.setModifiedAt(LocalDateTime.now());
        return report;
    }

    public void remove(Long id){
        reportRepository.removeReportById(id);
    }

    public Report checkStatus(Long id, Boolean status) throws IllegalAccessException {
        Optional<Report> byId = reportRepository.findById(id);
        if(byId.isEmpty()){
            throw new IllegalAccessException("해당 report를 찾을 수 없습니다");
        }

        Report report = byId.get();
        if(status) {
            report.setIsChecked(true);
        } else{
            report.setIsChecked(false);
        }
        return report;
    }

}
