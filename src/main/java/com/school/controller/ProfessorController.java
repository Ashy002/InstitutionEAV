package com.school.controller;

import com.school.entity.Professor;
import com.school.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/professors")
public class ProfessorController {

  private final ProfessorService service;

  public ProfessorController(ProfessorService service) {
    this.service = service;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("professors", service.findAll());
    return "admin/professors/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("professor", new Professor());
    return "admin/professors/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("professor") Professor professor, BindingResult result, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) return "admin/professors/form";
    var creds = service.createWithAccount(professor);
    redirectAttributes.addFlashAttribute("createdAccount", creds);
    return "redirect:/admin/professors";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("professor", service.findById(id));
    return "admin/professors/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("professor") Professor professor, BindingResult result) {
    if (result.hasErrors()) return "admin/professors/form";
    professor.setId(id);
    service.save(professor);
    return "redirect:/admin/professors";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    service.deleteById(id);
    return "redirect:/admin/professors";
  }
}
