package kz.job4j.dreamjob.repository;

import kz.job4j.dreamjob.model.Candidate;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {
    private final AtomicInteger nextId = new AtomicInteger(0);
    private final ConcurrentMap<Integer, Candidate> candidates = new ConcurrentHashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Aidos", "student needs more practice", LocalDateTime.now(), 0));
        save(new Candidate(1, "Alex", "recently graduated from university", LocalDateTime.now(), 0));
        save(new Candidate(2, "Timur", "has a year of work experience", LocalDateTime.now(), 0));
        save(new Candidate(3, "Victor", "has two and a half years of work experience", LocalDateTime.now(), 0));
        save(new Candidate(4, "Vlad", "experienced developer", LocalDateTime.now(), 0));
        save(new Candidate(5, "Azamat", "experienced developer able to solve any problem", LocalDateTime.now(), 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
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
                                candidateToUpdate.getCreationDate(),
                                candidate.getFileId()
                                )) != null;
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