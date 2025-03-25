package com.example.Learn.LearnOne.Services;

import com.example.Learn.LearnOne.Entity.Voter;
import com.example.Learn.LearnOne.Repository.VoterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoterService {

    @Autowired
    private VoterRepository voterRepository;

    public void saveVoter(Voter voter) {
        voterRepository.save(voter);
    }

    public Optional<Voter> getVoter(String voterId) {
        return voterRepository.findByVoterId(voterId);
    }

    public List<Voter> findAllVoters() {
        return voterRepository.findAll();
    }

    public void updateVoter(Voter voter) {
        if (voterRepository.existsById(voter.getId())) {
            voterRepository.save(voter);
        }
    }

    public void deleteVoter(Long id) {
        voterRepository.deleteById(id);
    }

    public List<Voter> findVotersByBranch(String branch) {
        return voterRepository.findByBranch(branch);
    }

    public List<Voter> findVotersByPollingStation(String pollingStation) {
        return voterRepository.findByPollingStation(pollingStation);
    }

    public List<Voter> findActiveVoters(boolean active) {
        return voterRepository.findByActive(active);
    }

    public List<Voter> searchVotersByName(String name) {
        return voterRepository.findByFullNameContainingIgnoreCase(name);
    }

    @Override
    public String toString() {
        return "VoterService{" + "voterRepository=" + voterRepository + '}';
    }
}