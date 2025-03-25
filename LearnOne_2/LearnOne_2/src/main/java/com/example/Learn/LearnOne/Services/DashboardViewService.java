package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Entity.Welfare;
import com.example.Learn.LearnOne.Repository.VoterRepository;
import com.example.Learn.LearnOne.Repository.WelfareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardViewService {

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private WelfareRepository welfareRepository;

    public int getTotalVoters() {
        return (int) voterRepository.count();
    }

    public int[] getAgeDistribution() {
        int[] ageDistribution = new int[6]; // Age groups
        List<Voter> voters = voterRepository.findAll();

        for (Voter voter : voters) {
            if (voter.getAge() >= 18 && voter.getAge() <= 24) ageDistribution[0]++;
            else if (voter.getAge() >= 25 && voter.getAge() <= 34) ageDistribution[1]++;
            else if (voter.getAge() >= 35 && voter.getAge() <= 44) ageDistribution[2]++;
            else if (voter.getAge() >= 45 && voter.getAge() <= 54) ageDistribution[3]++;
            else if (voter.getAge() >= 55 && voter.getAge() <= 64) ageDistribution[4]++;
            else if (voter.getAge() >= 65) ageDistribution[5]++;
        }

        return ageDistribution;
    }

    public int[] getGenderDistribution() {
        int[] genderDistribution = new int[2]; // Male, Female
        List<Voter> voters = voterRepository.findAll();

        for (Voter voter : voters) {
            if ("Male".equalsIgnoreCase(voter.getGender())) genderDistribution[0]++;
            else if ("Female".equalsIgnoreCase(voter.getGender())) genderDistribution[1]++;
        }

        return genderDistribution;
    }

    public Map<String, Long> getWardDistribution() {
        List<Welfare> welfareData = welfareRepository.findAll();
        return welfareData.stream()
                .collect(Collectors.groupingBy(Welfare::getWard, Collectors.counting()));
    }

    public List<Voter> getVoterList() {
        return voterRepository.findAll();
    }
}