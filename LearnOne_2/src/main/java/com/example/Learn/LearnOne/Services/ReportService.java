package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Welfare;
import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Repository.WelfareRepository;
import com.example.Learn.LearnOne.Repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private WelfareRepository welfareRepository;

    @Autowired
    private VoterRepository voterRepository;

    // Total counts
    public long getTotalBeneficiaries() {
        return welfareRepository.count();
    }

    public long getTotalVoters() {
        return voterRepository.count();
    }

    // Beneficiaries by ward
    public Map<String, Long> getBeneficiariesByWard() {
        List<Welfare> welfares = welfareRepository.findAll();
        return welfares.stream()
                .collect(Collectors.groupingBy(Welfare::getWard, Collectors.counting()));
    }

    // Voters by polling station
    public Map<String, Long> getVotersByPollingStation() {
        List<Voter> voters = voterRepository.findAll();
        return voters.stream()
                .collect(Collectors.groupingBy(Voter::getPollingStation, Collectors.counting()));
    }

    // Total amount paid by ward
    public Map<String, Double> getTotalAmountPaidByWard() {
        List<Welfare> welfares = welfareRepository.findAll();
        return welfares.stream()
                .collect(Collectors.groupingBy(Welfare::getWard, Collectors.summingDouble(Welfare::getAmountPaid)));
    }

    // Welfare payment status breakdown
    public Map<String, Long> getPaymentStatusBreakdown() {
        List<Welfare> welfares = welfareRepository.findAll();
        return welfares.stream()
                .collect(Collectors.groupingBy(Welfare::getPaymentStatus, Collectors.counting()));
    }

    // Overdue welfare records
    public List<Welfare> getOverdueWelfares() {
        return welfareRepository.findByDueDateBeforeAndPaymentStatusNot(LocalDate.now(), "Paid");
    }

    // Filtered reports by date range and ward
    public List<Welfare> getWelfareByDateRangeAndWard(LocalDate startDate, LocalDate endDate, String ward) {
        List<Welfare> welfares = (ward == null || ward.isEmpty())
                ? welfareRepository.findAll()
                : welfareRepository.findByWard(ward);
        return welfares.stream()
                .filter(w -> !w.getStartDate().isBefore(startDate) && !w.getStartDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    // Summary report data
    public Map<String, Object> getSummaryReport(LocalDate startDate, LocalDate endDate, String ward) {
        Map<String, Object> report = new HashMap<>();
        List<Welfare> filteredWelfares = getWelfareByDateRangeAndWard(startDate, endDate, ward);
        List<Voter> filteredVoters = (ward == null || ward.isEmpty())
                ? voterRepository.findAll()
                : voterRepository.findByPollingStation(ward); // Updated to use pollingStation

        report.put("totalBeneficiaries", filteredWelfares.size());
        report.put("totalVoters", filteredVoters.size());
        report.put("totalAmountPaid", filteredWelfares.stream().mapToDouble(Welfare::getAmountPaid).sum());
        report.put("beneficiariesByWard", filteredWelfares.stream()
                .collect(Collectors.groupingBy(Welfare::getWard, Collectors.counting())));
        report.put("votersByPollingStation", filteredVoters.stream() // Updated to use pollingStation
                .collect(Collectors.groupingBy(Voter::getPollingStation, Collectors.counting())));
        report.put("paymentStatusBreakdown", filteredWelfares.stream()
                .collect(Collectors.groupingBy(Welfare::getPaymentStatus, Collectors.counting())));
        report.put("overdueCount", filteredWelfares.stream()
                .filter(w -> w.getDueDate() != null && w.getDueDate().isBefore(LocalDate.now()) && !"Paid".equals(w.getPaymentStatus()))
                .count());

        return report;
    }
}