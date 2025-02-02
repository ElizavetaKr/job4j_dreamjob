package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.service.CandidateService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CandidateControllerTest {

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
    public void whenGetAllThenPageWithCandidates() {
        Candidate candidate1 = new Candidate(1, "candidate1", "desc1", 1);
        Candidate candidate2 = new Candidate(2, "candidate2", "desc2", 2);
        var expectedCandidates = List.of(candidate1, candidate2);
        when(candidateService.findAll()).thenReturn(expectedCandidates);

        Model model = new ConcurrentModel();

        assertThat(candidateController.getAll(model)).isEqualTo("candidates/list");
        assertThat(model.getAttribute("candidates")).isEqualTo(expectedCandidates);
    }

    @Test
    public void whenGetCreationPageThenPageWithCandidatesCreate() {
        assertThat(candidateController.getCreationPage(new ConcurrentModel())).isEqualTo("candidates/create");
    }

    @Test
    public void whenCreateThenPageWithCandidates() throws Exception {
        Candidate candidate = new Candidate(1, "candidate", "desc", 1);
        FileDto fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.save(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(candidate);

        Model model = new ConcurrentModel();
        var result = candidateController.create(candidate, testFile, model);
        var actualCandidate = candidateArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(result).isEqualTo("redirect:/candidates");
        assertThat(actualCandidate).isEqualTo(candidate);
        assertThat(actualFileDto).usingRecursiveComparison().isEqualTo(fileDto);
    }

    @Test
    public void whenCreateWithExceptionThenErrorPageWithMessage() {
        Exception exception = new RuntimeException("Message");
        when(candidateService.save(any(), any())).thenThrow(exception);

        Model model = new ConcurrentModel();

        assertThat(candidateController.create(new Candidate(), testFile, model)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo(exception.getMessage());
    }

    @Test
    public void whenUpdateThenPageWithCandidates() throws Exception {
        Candidate candidate = new Candidate(1, "candidate", "desc", 1);
        FileDto fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var candidateArgumentCaptor = ArgumentCaptor.forClass(Candidate.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(candidateService.update(candidateArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(true);

        Model model = new ConcurrentModel();
        var result = candidateController.update(candidate, testFile, model);
        var actualCandidate = candidateArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(result).isEqualTo("redirect:/candidates");
        assertThat(actualCandidate).isEqualTo(candidate);
        assertThat(actualFileDto).usingRecursiveComparison().isEqualTo(fileDto);
    }

    @Test
    public void whenUpdateIsFalseWithExceptionThenErrorPageWithMessage() {
        Model model = new ConcurrentModel();

        assertThat(candidateController.update(new Candidate(), testFile, model)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("Кандидат с указанным идентификатором не найден");
    }

    @Test
    public void whenUpdateWithExceptionThenErrorPageWithMessage() {
        Exception exception = new RuntimeException("Message");
        when(candidateService.update(any(), any())).thenThrow(exception);

        Model model = new ConcurrentModel();

        assertThat(candidateController.update(new Candidate(), testFile, model)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo(exception.getMessage());
    }

    @Test
    public void whenGetByIdThenPageWithCandidate() {
        Candidate candidate = new Candidate(1, "candidate1", "desc1", 1);
        when(candidateService.findById(1)).thenReturn(Optional.of(candidate));

        Model model = new ConcurrentModel();

        assertThat(candidateController.getById(model, 1)).isEqualTo("candidates/one");
        assertThat(model.getAttribute("candidate")).isEqualTo(candidate);
    }

    @Test
    public void whenCandidateNotExistThenErrorPageWithMessage() {
        Model model = new ConcurrentModel();
        assertThat(candidateController.getById(model, 1)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("Кандидат с указанным идентификатором не найден");
    }

    @Test
    public void whenDeleteThenPageWithCandidate() {
        when(candidateService.deleteById(1)).thenReturn(true);

        assertThat(candidateController.delete(new ConcurrentModel(), 1)).isEqualTo("redirect:/candidates");
    }

    @Test
    public void whenDeleteIsFalseThenErrorPageWithMessage() {
        Model model = new ConcurrentModel();
        assertThat(candidateController.delete(model, 1)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("Кандидат с указанным идентификатором не найден");
    }
}