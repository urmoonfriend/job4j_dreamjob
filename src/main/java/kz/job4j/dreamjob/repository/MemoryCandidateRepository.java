package kz.job4j.dreamjob.repository;

import kz.job4j.dreamjob.model.Candidate;
import kz.job4j.dreamjob.model.Vacancy;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {

    private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();

    private int nextId = 1;

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Aidos", "student needs more practice", LocalDateTime.now()));
        save(new Candidate(1, "Alex", "recently graduated from university", LocalDateTime.now()));
        save(new Candidate(2, "Timur", "has a year of work experience", LocalDateTime.now()));
        save(new Candidate(3, "Victor", "has two and a half years of work experience", LocalDateTime.now()));
        save(new Candidate(4, "Vlad", "experienced developer", LocalDateTime.now()));
        save(new Candidate(5, "Azamat", "experienced developer able to solve any problem", LocalDateTime.now()));
    }

    public static MemoryCandidateRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(
                candidate.getId(), (id, candidateToUpdate) ->
                        new Candidate(candidateToUpdate.getId(),
                                candidate.getName(),
                                candidate.getDescription(),
                                candidateToUpdate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }

}