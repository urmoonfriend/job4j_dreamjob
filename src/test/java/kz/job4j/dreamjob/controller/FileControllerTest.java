package kz.job4j.dreamjob.controller;

import kz.job4j.dreamjob.model.FileDto;
import kz.job4j.dreamjob.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class FileControllerTest {
    private FileService fileService;
    private FileController filerController;
    private MultipartFile testFile;

    @BeforeEach
    public void initServices() {
        fileService = mock(FileService.class);
        filerController = new FileController(fileService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
    }

    @Test
    public void whenRequestGetByIdThenGetContent() throws IOException {
        FileDto fileDto = new FileDto(testFile.getName(), testFile.getBytes());
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.of(fileDto));

        var response = filerController.getById(1);
        var responseBody = response.getBody();

        assertThat(responseBody).isEqualTo(fileDto.getContent());
    }

    @Test
    public void whenRequestGetByIdThenError() {
        when(fileService.getFileById(any(Integer.class))).thenReturn(Optional.empty());
        var response = filerController.getById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}