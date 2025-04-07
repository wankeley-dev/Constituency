package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Welfare;
import com.example.Learn.LearnOne.Entity.WelfareAssistance;
import com.example.Learn.LearnOne.Repository.WelfareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WelfareService {

    @Autowired
    private WelfareRepository welfareRepository;

    public Welfare saveOrUpdateWelfare(Welfare welfare) {
        Optional<Welfare> existingWelfareOpt = welfareRepository.findByVoter_VoterId(welfare.getVoter().getVoterId());
        if (existingWelfareOpt.isPresent() && welfare.getId() == null) {
            Welfare existingWelfare = existingWelfareOpt.get();
            existingWelfare.setName(welfare.getName());
            existingWelfare.setWard(welfare.getWard());
            existingWelfare.setAmountPaid(welfare.getAmountPaid());
            existingWelfare.setStartDate(welfare.getStartDate());
            existingWelfare.setDueDate(welfare.getDueDate());
            existingWelfare.setPaymentStatus(welfare.getPaymentStatus());
            existingWelfare.setNotes(welfare.getNotes());
            existingWelfare.setVoter(welfare.getVoter());
            existingWelfare.setCategory(welfare.getCategory());
            return welfareRepository.save(existingWelfare);
        } else {
            return welfareRepository.save(welfare);
        }
    }

    public void addAssistance(Long welfareId, WelfareAssistance assistance) {
        Welfare welfare = welfareRepository.findById(welfareId)
                .orElseThrow(() -> new IllegalArgumentException("Welfare not found"));
        assistance.setWelfare(welfare);
        if (welfare.getAssistanceHistory() == null) {
            welfare.setAssistanceHistory(new ArrayList<>());
        }
        welfare.getAssistanceHistory().add(assistance);
        welfare.setAmountPaid(welfare.getAmountPaid() + assistance.getAmount());
        welfareRepository.save(welfare);
    }

    public List<Welfare> filterWelfares(String ward, String paymentStatus, Welfare.BeneficiaryCategory category, LocalDate startDate, LocalDate endDate) {
        if (ward != null && !ward.isEmpty()) {
            return welfareRepository.findByWard(ward);
        }
        if (paymentStatus != null && !paymentStatus.isEmpty()) {
            return welfareRepository.findByPaymentStatus(paymentStatus);
        }
        if (category != null) {
            return welfareRepository.findByCategory(category);
        }
        if (startDate != null && endDate != null) {
            return welfareRepository.findByDateRange(startDate, endDate);
        }
        return welfareRepository.findAll();
    }

    public List<Welfare> findOverdueWelfares() {
        return welfareRepository.findByDueDateBeforeAndPaymentStatusNot(LocalDate.now(), "Paid");
    }

    public Map<String, Object> generateCategoryReport() {
        List<Object[]> results = welfareRepository.getCategoryDistributionReport();
        Map<String, Object> report = new HashMap<>();
        List<String> categories = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        List<Double> totals = new ArrayList<>();

        for (Object[] result : results) {
            categories.add(result[0].toString());
            counts.add((Long) result[1]);
            totals.add((Double) result[2]);
        }

        report.put("categories", categories);
        report.put("counts", counts);
        report.put("totals", totals);
        return report;
    }

    public Map<String, Object> generateWardReport(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = welfareRepository.getWardDistributionReport(startDate, endDate);
        Map<String, Object> report = new HashMap<>();
        List<String> wards = new ArrayList<>();
        List<Long> counts = new ArrayList<>();
        List<Double> totals = new ArrayList<>();

        for (Object[] result : results) {
            wards.add((String) result[0]);
            counts.add((Long) result[1]);
            totals.add((Double) result[2]);
        }

        report.put("wards", wards);
        report.put("counts", counts);
        report.put("totals", totals);
        report.put("startDate", startDate);
        report.put("endDate", endDate);
        return report;
    }

    public Optional<Welfare> getWelfareById(Long id) {
        return welfareRepository.findById(id);
    }

    public void deleteWelfare(Long id) {
        welfareRepository.deleteById(id);
    }

    public List<Welfare> findAllWelfares() {
        return welfareRepository.findAll();
    }
}