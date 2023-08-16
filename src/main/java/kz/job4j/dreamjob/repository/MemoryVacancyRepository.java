package kz.job4j.dreamjob.repository;

import kz.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryVacancyRepository implements VacancyRepository {
    
    private static final MemoryVacancyRepository INSTANCE = new MemoryVacancyRepository();    

    private int nextId = 1;

    private final Map<Integer, Vacancy> vacancies = new HashMap<>();

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer", "need to know Java Core", LocalDateTime.now()));
        save(new Vacancy(1, "Junior Java Developer", "need to know Strategy Patterns, simple SQL", LocalDateTime.now()));
        save(new Vacancy(2, "Junior+ Java Developer", "need to know Spring Framework, work with DB", LocalDateTime.now()));
        save(new Vacancy(3, "Middle Java Developer", "need to know Concurrency, Deploy, Web, DB, Frameworks, work experience more than 2 years", LocalDateTime.now()));
        save(new Vacancy(4, "Middle+ Java Developer", "need to know build Architecture, big experience", LocalDateTime.now()));
        save(new Vacancy(5, "Senior Java Developer", "need to solve big problems, build Architecture or application, task delegation, development team management", LocalDateTime.now()));
    }

    public static MemoryVacancyRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Vacancy save(Vacancy vacancy) {
        vacancy.setId(nextId++);
        vacancies.put(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public boolean deleteById(int id) {
        return vacancies.remove(id) != null;
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(
                vacancy.getId(), (id, oldVacancy) ->
                        new Vacancy(oldVacancy.getId(),
                                vacancy.getTitle(),
                                vacancy.getDescription(),
                                vacancy.getCreationDate())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }

}