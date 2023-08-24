package kz.job4j.dreamjob.service;

import kz.job4j.dreamjob.model.Candidate;
import kz.job4j.dreamjob.model.FileDto;

import java.util.Collection;
import java.util.Optional;

public interface CandidateService {

    Candidate save(Candidate candidate, FileDto image);

    void deleteById(int id);

    boolean update(Candidate candidate, FileDto image);

    Optional<Candidate> findById(int id);

    Collection<Candidate> findAll();

}