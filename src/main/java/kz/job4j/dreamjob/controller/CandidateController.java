package kz.job4j.dreamjob.controller;

import kz.job4j.dreamjob.model.Candidate;
import kz.job4j.dreamjob.service.CandidateService;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicReference;

@Controller
@ThreadSafe
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;
    private static final String REDIRECT_CANDIDATES = "redirect:/candidates";
    private static final String NOT_FOUND_PAGE = "errors/404";
    private static final String NOT_FOUND_MESSAGE = "Резюме с указанным идентификатором не найдено";
    private static final String MESSAGE_ATTRIBUTE = "message";

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "candidates/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Candidate candidate) {
        candidateService.save(candidate);
        return REDIRECT_CANDIDATES;
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        AtomicReference<String> page = new AtomicReference<>(NOT_FOUND_PAGE);
        candidateService.findById(id).ifPresentOrElse(
                candidate -> {
                    model.addAttribute("candidate", candidate);
                    page.set("candidates/one");
                }, () -> model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE)
        );
        return page.get();
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Candidate candidate, Model model) {
        AtomicReference<String> page = new AtomicReference<>(NOT_FOUND_PAGE);
        candidateService.findById(candidate.getId()).ifPresentOrElse(
                candidateToUpdate -> {
                    candidateService.update(candidate);
                    page.set(REDIRECT_CANDIDATES);
                }, () -> model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE)
        );
        return page.get();
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        AtomicReference<String> page = new AtomicReference<>(NOT_FOUND_PAGE);
        candidateService.findById(id).ifPresentOrElse(
                candidateToDelete -> {
                    candidateService.deleteById(id);
                    page.set(REDIRECT_CANDIDATES);
                }, () -> model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE)
        );
        return page.get();
    }
}
