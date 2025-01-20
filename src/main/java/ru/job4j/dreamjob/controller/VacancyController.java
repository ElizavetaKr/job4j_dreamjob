package ru.job4j.dreamjob.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.Vacancy;
import ru.job4j.dreamjob.repository.MemoryVacancyRepository;
import ru.job4j.dreamjob.repository.VacancyRepository;

@Controller
@RequestMapping("/vacancies")
/** Работать с кандидатами будем по URI /vacancies/** */
public class VacancyController {

    private final VacancyRepository vacancyRepository = MemoryVacancyRepository.getInstance();

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("vacancies", vacancyRepository.findAll());
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "vacancies/create";
    }

    /**
     * @PostMapping("/create") public String create(HttpServletRequest request) {
     * var title = request.getParameter("title");
     * var description = request.getParameter("description");
     * vacancyRepository.save(new Vacancy(0, title, description));
     * return "redirect:/vacancies";
     * }
     */

    @PostMapping("/create")
    public String create(@ModelAttribute Vacancy vacancy) {
        vacancyRepository.save(vacancy);
        return "redirect:/vacancies";
    }

    /**извлекает вакансию из репозитория и возвращает на страницу*/
    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var vacancyOptional = vacancyRepository.findById(id);
        if (vacancyOptional.isEmpty()) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("vacancy", vacancyOptional.get());
        return "vacancies/one";
    }

    /**производит обновление и если оно произошло, то делает перенаправление на страницу со всеми вакансиями*/
    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy, Model model) {
        var isUpdated = vacancyRepository.update(vacancy);
        if (!isUpdated) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vacancies";
    }

    /**роизводит удаление и если оно произошло, то делает перенаправление на страницу со всеми вакансиями*/
    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var deleted = vacancyRepository.findById(id);
         vacancyRepository.deleteById(id);
        if (deleted.isEmpty()) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vacancies";
    }
}