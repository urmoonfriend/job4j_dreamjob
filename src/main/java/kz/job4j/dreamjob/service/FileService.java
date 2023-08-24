package kz.job4j.dreamjob.service;

import kz.job4j.dreamjob.model.File;
import kz.job4j.dreamjob.model.FileDto;

import java.util.Optional;

public interface FileService {
    File save(FileDto fileDto);

    Optional<FileDto> getFileById(int id);

    void deleteById(int id);
}
