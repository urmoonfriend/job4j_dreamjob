package kz.job4j.dreamjob.service;

import kz.job4j.dreamjob.model.Vacancy;

import java.util.Collection;
import java.util.Optional;

public interface VacancyService {

    Vacancy save(Vacancy vacancy);

    void deleteById(int id);

    boolean update(Vacancy vacancy);

    Optional<Vacancy> findById(int id);

    Collection<Vacancy> findAll();

}