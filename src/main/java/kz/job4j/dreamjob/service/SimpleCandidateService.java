package kz.job4j.dreamjob.service;

import kz.job4j.dreamjob.model.Candidate;
import kz.job4j.dreamjob.repository.CandidateRepository;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@ThreadSafe
public class SimpleCandidateService implements CandidateService {
    private final CandidateRepository candidateRepository;

    public SimpleCandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @Override
    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public void deleteById(int id) {
        candidateRepository.deleteById(id);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidateRepository.update(candidate);
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return candidateRepository.findById(id);
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidateRepository.findAll();
    }

}