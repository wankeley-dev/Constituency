package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Event;
import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Entity.VoterEventParticipation;
import com.example.Learn.LearnOne.Entity.Welfare;
import com.example.Learn.LearnOne.Repository.EventRepository;
import com.example.Learn.LearnOne.Repository.VoterEventParticipationRepository;
import com.example.Learn.LearnOne.Repository.VoterRepository;
import com.example.Learn.LearnOne.Repository.WelfareRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class adminDashboardService {

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private WelfareRepository welfareRepository;

    @Autowired
    private VoterEventParticipationRepository participationRepository;

    @Autowired
    private EventRepository eventRepository;

    public int getTotalVoters() {
        return (int) voterRepository.count();
    }

    public Map<String, Integer> getAgeDistribution() {
        Map<String, Integer> ageDistribution = new HashMap<>();
        ageDistribution.put("18-24", 0);
        ageDistribution.put("25-34", 0);
        ageDistribution.put("35-44", 0);
        ageDistribution.put("45-54", 0);
        ageDistribution.put("55-64", 0);
        ageDistribution.put("65+", 0);

        List<Voter> voters = voterRepository.findAll();
        for (Voter voter : voters) {
            int age = voter.getAge();
            if (age >= 18 && age <= 24) ageDistribution.put("18-24", ageDistribution.get("18-24") + 1);
            else if (age >= 25 && age <= 34) ageDistribution.put("25-34", ageDistribution.get("25-34") + 1);
            else if (age >= 35 && age <= 44) ageDistribution.put("35-44", ageDistribution.get("35-44") + 1);
            else if (age >= 45 && age <= 54) ageDistribution.put("45-54", ageDistribution.get("45-54") + 1);
            else if (age >= 55 && age <= 64) ageDistribution.put("55-64", ageDistribution.get("55-64") + 1);
            else if (age >= 65) ageDistribution.put("65+", ageDistribution.get("65+") + 1);
        }
        return ageDistribution;
    }

    public Map<String, Integer> getGenderDistribution() {
        Map<String, Integer> genderDistribution = new HashMap<>();
        genderDistribution.put("Male", 0);
        genderDistribution.put("Female", 0);

        List<Voter> voters = voterRepository.findAll();
        for (Voter voter : voters) {
            String gender = voter.getGender();
            if ("Male".equalsIgnoreCase(gender)) genderDistribution.put("Male", genderDistribution.get("Male") + 1);
            else if ("Female".equalsIgnoreCase(gender)) genderDistribution.put("Female", genderDistribution.get("Female") + 1);
        }
        return genderDistribution;
    }

    public Map<String, Integer> getEmploymentDistribution() {
        Map<String, Integer> employmentDistribution = new HashMap<>();
        for (Voter.EmploymentStatus status : Voter.EmploymentStatus.values()) {
            employmentDistribution.put(status.name(), 0);
        }

        List<Voter> voters = voterRepository.findAll();
        for (Voter voter : voters) {
            if (voter.getEmploymentStatus() != null) {
                employmentDistribution.put(voter.getEmploymentStatus().name(),
                        employmentDistribution.get(voter.getEmploymentStatus().name()) + 1);
            }
        }
        return employmentDistribution;
    }

    public Map<String, Long> getWardDistribution() {
        List<Welfare> welfareData = welfareRepository.findAll();
        return welfareData.stream()
                .collect(Collectors.groupingBy(Welfare::getWard, Collectors.counting()));
    }

    public Map<String, Integer> getEventParticipationByWard() {
        List<Event> events = eventRepository.findAll();
        return events.stream()
                .collect(Collectors.groupingBy(Event::getWard,
                        Collectors.summingInt(Event::getParticipantCount)));
    }

    public Map<String, Long> getWelfareCategoryDistribution() {
        List<Welfare> welfareData = welfareRepository.findAll();
        return welfareData.stream()
                .collect(Collectors.groupingBy(w -> w.getCategory().name(), Collectors.counting()));
    }

    public Map<String, Integer> getEventParticipation() {
        List<VoterEventParticipation> participations = participationRepository.findAll();
        return participations.stream()
                .collect(Collectors.groupingBy(p -> p.getEvent().getEventName(),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
    }

    public List<Voter> getVoterList() {
        return voterRepository.findAll();
    }

    // New methods to calculate sums
    public long getTotalWelfareBeneficiaries() {
        return getWelfareCategoryDistribution().values().stream().mapToLong(Long::longValue).sum();
    }

    public int getTotalEventParticipants() {
        return getEventParticipation().values().stream().mapToInt(Integer::intValue).sum();
    }
}