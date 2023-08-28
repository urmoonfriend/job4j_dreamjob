package kz.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import kz.job4j.dreamjob.model.City;
import kz.job4j.dreamjob.repository.CityRepository;

import java.util.Collection;

@Service
@ThreadSafe
public class SimpleCityService implements CityService {

    private final CityRepository cityRepository;

    public SimpleCityService(CityRepository sql2oCityRepository) {
        this.cityRepository = sql2oCityRepository;
    }

    @Override
    public Collection<City> findAll() {
        return cityRepository.findAll();
    }
}