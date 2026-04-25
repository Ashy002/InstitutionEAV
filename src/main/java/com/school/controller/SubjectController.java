package com.school.controller;

import com.school.entity.Subject;
import com.school.service.ProfessorService;
import com.school.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/subjects")
public class SubjectController {

  private final SubjectService subjectService;
  private final ProfessorService professorService;

  public SubjectController(SubjectService subjectService, ProfessorService professorService) {
    this.subjectService = subjectService;
    this.professorService = professorService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("subjects", subjectService.findAll());
    return "admin/subjects/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("subject", new Subject());
    model.addAttribute("professors", professorService.findAll());
    return "admin/subjects/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("subject") Subject subject, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("professors", professorService.findAll());
      return "admin/subjects/form";
    }
    subjectService.save(subject);
    return "redirect:/admin/subjects";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("subject", subjectService.findById(id));
    model.addAttribute("professors", professorService.findAll());
    return "admin/subjects/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("subject") Subject subject, BindingResult result, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("professors", professorService.findAll());
      return "admin/subjects/form";
    }
    subject.setId(id);
    subjectService.save(subject);
    return "redirect:/admin/subjects";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    subjectService.deleteById(id);
    return "redirect:/admin/subjects";
  }
}
