package kz.job4j.dreamjob.repository;

import kz.job4j.dreamjob.model.File;

import java.util.Optional;

public interface FileRepository {
    File save(File file);

    Optional<File> findById(int id);

    void deleteById(int id);
}
