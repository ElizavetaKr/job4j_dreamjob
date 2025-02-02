package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.City;
import ru.job4j.dreamjob.model.Vacancy;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.VacancyService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VacancyControllerTest {
    private VacancyService vacancyService;

    private CityService cityService;

    private VacancyController vacancyController;

    private MultipartFile testFile;

    @BeforeEach
    public void initServices() {
        vacancyService = mock(VacancyService.class);
        cityService = mock(CityService.class);
        vacancyController = new VacancyController(vacancyService, cityService);
        testFile = new MockMultipartFile("testFile.img", new byte[]{1, 2, 3});
    }

    @Test
    public void whenRequestVacancyListPageThenGetPageWithVacancies() {
        Vacancy vacancy1 = new Vacancy(1, "vacancy1", "desc1", true, 1, 2);
        Vacancy vacancy2 = new Vacancy(2, "vacancy2", "desc2", false, 3, 4);
        var expectedVacancy = List.of(vacancy1, vacancy2);
        when(vacancyService.findAll()).thenReturn(expectedVacancy);

        Model model = new ConcurrentModel();
        String view = vacancyController.getAll(model);
        var actualVacancies = model.getAttribute("vacancies");

        assertThat(view).isEqualTo("vacancies/list");
        assertThat(actualVacancies).isEqualTo(expectedVacancy);
    }

    @Test
    public void whenRequestVacancyCreationPageThenGetPageWithCities() {
        City city1 = new City(1, "city1");
        City city2 = new City(2, "city2");
        var expectedCities = List.of(city1, city2);
        when(cityService.findAll()).thenReturn(expectedCities);

        Model model = new ConcurrentModel();

        assertThat(vacancyController.getCreationPage(model)).isEqualTo("vacancies/create");
        assertThat(model.getAttribute("cities")).isEqualTo(expectedCities);
    }

    @Test
    public void whenPostVacancyWithFileThenSameDataAndRedirectToVacanciesPage() throws Exception {
        Vacancy vacancy = new Vacancy(1, "vacancy1", "desc1", true, 1, 2);
        FileDto fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        var vacancyArgumentCaptor = ArgumentCaptor.forClass(Vacancy.class);
        var fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(vacancyService.save(vacancyArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(vacancy);

        Model model = new ConcurrentModel();
        var view = vacancyController.create(vacancy, testFile, model);
        var actualVacancy = vacancyArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualVacancy).isEqualTo(vacancy);
        assertThat(actualFileDto).usingRecursiveComparison().isEqualTo(fileDto);
    }

    @Test
    public void whenSomeExceptionThrownThenGetErrorPageWithMessage() {
        var expectedException = new RuntimeException("Failed to write file");
        when(vacancyService.save(any(), any())).thenThrow(expectedException);

        var model = new ConcurrentModel();
        var view = vacancyController.create(new Vacancy(), testFile, model);
        var actualExceptionMessage = model.getAttribute("message");

        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenGetByIdThenGetPageWithVacancies() {
        City city1 = new City(1, "city1");
        City city2 = new City(2, "city2");
        Optional<Vacancy> vacancy1 = Optional.of(new Vacancy(1, "vacancy1", "desc1", true, 1, 2));
        var expectedCities = List.of(city1, city2);
        when(cityService.findAll()).thenReturn(expectedCities);
        when(vacancyService.findById(1)).thenReturn(vacancy1);

        Model model = new ConcurrentModel();

        assertThat(vacancyController.getById(model, 1)).isEqualTo("vacancies/one");
        assertThat(model.getAttribute("cities")).isEqualTo(expectedCities);
        assertThat(model.getAttribute("vacancy")).isEqualTo(vacancy1.get());

    }

    @Test
    public void whenVacancyOptionalIsEmptyThenGetErrorPageWithMessage() {
        Model model = new ConcurrentModel();

        assertThat(vacancyController.getById(model, 1)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("Вакансия с указанным идентификатором не найдена");
    }

    @Test
    public void whenUpdateThenGetPageWithVacancies() throws Exception {
        Vacancy vacancy = new Vacancy(1, "vacancy1", "desc1", true, 1, 2);
        FileDto fileDto = new FileDto(testFile.getOriginalFilename(), testFile.getBytes());
        ArgumentCaptor<Vacancy> vacancyArgumentCaptor = ArgumentCaptor.forClass(Vacancy.class);
        ArgumentCaptor<FileDto> fileDtoArgumentCaptor = ArgumentCaptor.forClass(FileDto.class);
        when(vacancyService.update(vacancyArgumentCaptor.capture(), fileDtoArgumentCaptor.capture())).thenReturn(true);

        Model model = new ConcurrentModel();
        var view = vacancyController.update(vacancy, testFile, model);
        var actualVacancy = vacancyArgumentCaptor.getValue();
        var actualFileDto = fileDtoArgumentCaptor.getValue();

        assertThat(view).isEqualTo("redirect:/vacancies");
        assertThat(actualVacancy).isEqualTo(vacancy);
        assertThat(fileDto).usingRecursiveComparison().isEqualTo(actualFileDto);
    }

    @Test
    public void whenUpdateWithExceptionThenGetErrorPageWithMessage() {
        Exception expectedException = new RuntimeException("Some message");
        when(vacancyService.update(any(), any())).thenThrow(expectedException);

        Model model = new ConcurrentModel();

        assertThat(vacancyController.update(new Vacancy(), testFile, model)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenUpdateIsFalseWithExceptionThenGetErrorPageWithMessage() {
        Model model = new ConcurrentModel();
        assertThat(vacancyController.update(new Vacancy(), testFile, model)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("Вакансия с указанным идентификатором не найдена");
    }

    @Test
    public void whenDeleteThenGetPageWithVacancies() {
        when(vacancyService.deleteById(1)).thenReturn(true);

        Model model = new ConcurrentModel();

        assertThat(vacancyController.delete(model, 1)).isEqualTo("redirect:/vacancies");
    }

    @Test
    public void whenDeleteWithExceptionThenGetErrorPageWithMessage() {
        Model model = new ConcurrentModel();

        assertThat(vacancyController.delete(model, 1)).isEqualTo("errors/404");
        assertThat(model.getAttribute("message")).isEqualTo("Вакансия с указанным идентификатором не найдена");
    }
}