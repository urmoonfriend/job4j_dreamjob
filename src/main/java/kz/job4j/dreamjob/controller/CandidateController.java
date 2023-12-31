package kz.job4j.dreamjob.controller;

import kz.job4j.dreamjob.model.Candidate;
import kz.job4j.dreamjob.model.FileDto;
import kz.job4j.dreamjob.service.CandidateService;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@ThreadSafe
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;
    private static final String REDIRECT_CANDIDATES = "redirect:/candidates";
    private static final String NOT_FOUND_PAGE = "errors/404";
    private static final String NOT_FOUND_MESSAGE = "Резюме с указанным идентификатором не найдено";
    private static final String MESSAGE_ATTRIBUTE = "message";
    private static final String CANDIDATE_ATTRIBUTE = "candidate";
    private static final String GUEST = "Guest";

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
    public String create(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file, Model model) {
        try {
            candidateService.save(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return REDIRECT_CANDIDATES;
        } catch (Exception exception) {
            model.addAttribute(MESSAGE_ATTRIBUTE, exception.getMessage());
            return NOT_FOUND_PAGE;
        }
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        var candidateOptional = candidateService.findById(id);
        if (candidateOptional.isEmpty()) {
            model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE);
            return NOT_FOUND_PAGE;
        }
        model.addAttribute(CANDIDATE_ATTRIBUTE, candidateOptional.get());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file, Model model) {
        try {
            var isUpdated = candidateService.update(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdated) {
                model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE);
                return NOT_FOUND_PAGE;
            }
            return REDIRECT_CANDIDATES;
        } catch (Exception exception) {
            model.addAttribute(MESSAGE_ATTRIBUTE, exception.getMessage());
            return NOT_FOUND_PAGE;
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var candidateToDelete = candidateService.findById(id);
        if (candidateToDelete.isEmpty()) {
            model.addAttribute(MESSAGE_ATTRIBUTE, NOT_FOUND_MESSAGE);
            return NOT_FOUND_PAGE;
        }
        candidateService.deleteById(id);
        return REDIRECT_CANDIDATES;
    }
}
