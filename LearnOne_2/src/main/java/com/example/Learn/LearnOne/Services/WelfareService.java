package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Welfare;
import com.example.Learn.LearnOne.Repository.WelfareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
            return welfareRepository.save(existingWelfare);
        } else {
            return welfareRepository.save(welfare);
        }
    }

    public List<Welfare> findAllWelfares() {
        return welfareRepository.findAll();
    }

    public boolean existsByVoterId(String voterId) {
        return welfareRepository.existsByVoter_VoterId(voterId);
    }

    public Optional<Welfare> getWelfareById(Long id) {
        return welfareRepository.findById(id);
    }

    public void deleteWelfare(Long id) {
        welfareRepository.deleteById(id);
    }

    public List<Welfare> findWelfaresByWard(String ward) {
        return welfareRepository.findByWard(ward);
    }

    public List<Welfare> findWelfaresByPaymentStatus(String paymentStatus) {
        return welfareRepository.findByPaymentStatus(paymentStatus);
    }

    public List<Welfare> findOverdueWelfares() {
        return welfareRepository.findByDueDateBeforeAndPaymentStatusNot(LocalDate.now(), "Paid");
    }
}