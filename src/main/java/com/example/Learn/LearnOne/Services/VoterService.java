package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Users;
import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VoterService {

    @Autowired
    private VoterRepository voterRepository;

    public void saveVoter(Voter voter) {
        if (voter.getRegistrationDate() == null) {
            voter.setRegistrationDate(LocalDate.now());
        }
        voterRepository.save(voter); // Save even if user is null
    }

    public Optional<Voter> getVoter(String voterId) {
        return voterRepository.findByVoterId(voterId);
    }

    public Optional<Voter> getVoterByUser(Users user) {
        return voterRepository.findByUser(user);
    }

    public List<Voter> findAllVoters() {
        return voterRepository.findAll();
    }

    public void updateVoter(Voter voter) {
        if (voterRepository.existsById(voter.getId())) {
            voterRepository.save(voter);
        } else {
            throw new IllegalArgumentException("Voter with ID " + voter.getId() + " does not exist.");
        }
    }

    public void deleteVoter(Long id) {
        voterRepository.deleteById(id);
    }

    public List<Voter> filterVoters(String voterId, String branch, String pollingStation, Boolean active, String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return voterRepository.searchVoters(keyword);
        }
        if (voterId != null && !voterId.isEmpty()) {
            return voterRepository.findByVoterId(voterId).map(List::of).orElse(List.of());
        }
        if (branch != null && !branch.isEmpty()) {
            return voterRepository.findByBranch(branch);
        }
        if (pollingStation != null && !pollingStation.isEmpty()) {
            return voterRepository.findByPollingStation(pollingStation);
        }
        if (active != null) {
            return voterRepository.findByActive(active);
        }
        return voterRepository.findAll();
    }

    public Optional<Voter> getVoterById(Long id) {
        return voterRepository.findById(id);
    }
}