package kz.job4j.dreamjob.repository;

import java.util.Collection;
import kz.job4j.dreamjob.model.City;
public interface CityRepository {
    Collection<City> findAll();
}