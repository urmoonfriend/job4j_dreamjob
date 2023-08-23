package kz.job4j.dreamjob.service;

import kz.job4j.dreamjob.model.City;

import java.util.Collection;

public interface CityService {
    Collection<City> findAll();
}