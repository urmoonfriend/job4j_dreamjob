package kz.job4j.dreamjob.controller;

import kz.job4j.dreamjob.model.Vacancy;
import kz.job4j.dreamjob.service.CityService;
import kz.job4j.dreamjob.service.VacancyService;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@ThreadSafe
@RequestMapping("/vacancies")
public class VacancyController {
    private final VacancyService vacancyService;
    private final CityService cityService;
    private static final String REDIRECT_VACANCIES = "redirect:/vacancies";
    private static final String NOT_FOUND_PAGE = "errors/404";
    private static final String NOT_FOUND_MESSAGE = "Вакансия с указанным идентификатором не найдена";
    private static final String MESSAGE_ATTRIBUTE = "message";

    public VacancyController(VacancyService vacancyService, CityService cityService) {
        this.vacancyService = vacancyService;
        this.cityService = cityService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("vacancies", vacancyService.findAll());
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("cities", cityService.findAll());
        return "vacancies/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Vacancy vacancy) {
        vacancyService.save(vacancy);
        return REDIRECT_VACANCIES;
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        AtomicReference<String> page = new AtomicReference<>(NOT_FOUND_PAGE);
        vacancyService.findById(id).ifPresentOrElse(
                vacancy -> {
                    page.set("vacancies/one");
                    model.addAttribute("vacancy", vacancy);
                    model.addAttribute("cities", cityService.findAll());
                }, () -> model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE)
        );
        return page.get();
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy, Model model) {
        AtomicReference<String> page = new AtomicReference<>(NOT_FOUND_PAGE);
        vacancyService.findById(vacancy.getId()).ifPresentOrElse(
                vacancyToUpdate -> {
                    vacancyService.update(vacancy);
                    page.set(REDIRECT_VACANCIES);
                }, () -> model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE)
        );
        return page.get();
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        AtomicReference<String> page = new AtomicReference<>(NOT_FOUND_PAGE);
        vacancyService.findById(id).ifPresentOrElse(
                vacancyToDelete -> {
                    vacancyService.deleteById(id);
                    page.set(REDIRECT_VACANCIES);
                }, () -> model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE)
        );
        return page.get();
    }

}
