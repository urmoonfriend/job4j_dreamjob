package kz.job4j.dreamjob.controller;

import kz.job4j.dreamjob.model.Candidate;
import kz.job4j.dreamjob.model.FileDto;
import kz.job4j.dreamjob.service.CandidateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class CandidateControllerTest {
    private CandidateService candidateService;
    private CandidateController candidateController;
    private MultipartFile testFile;

    @BeforeEach
    public void initServices() {
        candidateService = mock(CandidateService.class);
        candidateController = new CandidateController(candidateService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
    }

    @Test
    public void whenRequestCandidateListPageThenGetPageWithCandidates() {
        var candidate1 = new Candidate(1, "test1", "desc1", now(), 1);
        var candidate2 = new Candidate(2, "test2", "desc2", now(), 3);
        var expectedCandidates = List.of(candidate1, candidate2);
        when(candidateService.findAll()).thenReturn(expectedCandidates);

        var model = new ConcurrentModel();
        var view = candidateController.getAll(model);
        var actualVacancies = model.getAttribute("candidates");

        assertThat(view).isEqualTo("candidates/list");
        assertThat(actualVacancies).isEqualTo(expectedCandidates);
    }

    @Test
    public void whenRequestCandidateCreationPageThenGetPageWithCandidateCreate() {
        var view = candidateController.getCreationPage();
        assertThat(view).isEqualTo("candidates/create");
    }

    @Test
    public void whenPostCandidateWithFileThenSameDataAndRedirectToCandidatesPage() throws Exception {
        var candidate = new Candidate(1, "test1", "desc1", now(), 2);
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.save(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(candidate);

        var model = new ConcurrentModel();
        var view = candidateController.create(candidate, testFile, model);
        var actualCandidate = candidateArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/candidates");
        assertThat(actualCandidate).isEqualTo(candidate);
        assertThat(fileDto).usingRecursiveComparison().isEqualTo(actualFileDto);

    }

    @Test
    public void whenSomeExceptionThrownThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("Failed to write file");
        when(candidateService.save(any(), any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = candidateController.create(new Candidate(), testFile, model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestCandidateGetByIdThenGetPageWithCandidate() {
        var candidate = new Candidate(1, "test1", "desc1", now(), 2);
        when(candidateService.findById(1)).thenReturn(Optional.of(candidate));

        var model = new ConcurrentModel();
        var view = candidateController.getById(model, 1);
        var actualCandidate = model.getAttribute("candidate");

        assertThat(view).isEqualTo("candidates/one");
        assertThat(actualCandidate).isEqualTo(candidate);
    }

    @Test
    public void whenRequestCandidateGetByIdThenError() {
        when(candidateService.findById(1)).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = candidateController.getById(model, 1);
        assertThat(view).isEqualTo("errors/404");
    }

    @Test
    public void whenUpdateCandidateWithFileThenSameDataAndRedirectToCandidatesPage() throws IOException {
        var candidate = new Candidate(1, "test1", "desc1", now(), 2);
        var fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        when(candidateService.update(any(), any())).thenReturn(true);

        var model = new ConcurrentModel();
        var view = candidateController.update(candidate, testFile, model);
        var actualCandidate = model.getAttribute("candidate");

        assertThat(view).isEqualTo("redirect:/candidates");
    }

    @Test
    public void whenUpdateCandidateWithFileThenError() {
        var expectedException = new RuntimeException("Failed to write file");
        when(candidateService.update(any(), any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = candidateController.update(new Candidate(), testFile, model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenRequestDeleteThenRedirectToCandidatesPage() {
        var candidate = new Candidate(1, "test1", "desc1", now(), 2);
        when(candidateService.findById(any(Integer.class))).thenReturn(Optional.of(candidate));
        doNothing().when(candidateService).deleteById(any(Integer.class));
        var model = new ConcurrentModel();
        var view = candidateController.delete(model, 1);
        assertThat(view).isEqualTo("redirect:/candidates");
    }

    @Test
    public void whenRequestDeleteThenNotFound() {
        when(candidateService.findById(any(Integer.class))).thenReturn(Optional.empty());
        doNothing().when(candidateService).deleteById(any(Integer.class));
        var model = new ConcurrentModel();
        var view = candidateController.delete(model, 1);
        assertThat(view).isEqualTo("errors/404");
    }

}