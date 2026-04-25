package com.school.controller;

import com.school.entity.Grade;
import com.school.service.GradeService;
import com.school.service.StudentService;
import com.school.service.SubjectService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/grades")
public class GradeController {

  private final GradeService gradeService;
  private final StudentService studentService;
  private final SubjectService subjectService;

  public GradeController(GradeService gradeService, StudentService studentService, SubjectService subjectService) {
    this.gradeService = gradeService;
    this.studentService = studentService;
    this.subjectService = subjectService;
  }

  @GetMapping
  public String list(Model model) {
    model.addAttribute("grades", gradeService.findAll());
    return "admin/grades/list";
  }

  @GetMapping("/new")
  public String createForm(@RequestParam(required = false) Long studentId, Model model) {
    Grade grade = new Grade();
    model.addAttribute("grade", grade);
    model.addAttribute("students", studentService.findAll());
    model.addAttribute("subjects", subjectService.findAll());
    model.addAttribute("studentId", studentId);
    // Passer l'étudiant séparément pour éviter tout problème de proxy Hibernate
    if (studentId != null) {
      model.addAttribute("selectedStudent", studentService.findById(studentId));
    }
    return "admin/grades/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("grade") Grade grade, BindingResult result,
      @RequestParam(required = false) Long studentId, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("students", studentService.findAll());
      model.addAttribute("subjects", subjectService.findAll());
      model.addAttribute("studentId", studentId);
      if (studentId != null) {
        model.addAttribute("selectedStudent", studentService.findById(studentId));
      }
      return "admin/grades/form";
    }
    gradeService.save(grade);
    if (studentId != null) return "redirect:/admin/students/" + studentId + "/bulletin";
    return "redirect:/admin/grades";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, @RequestParam(required = false) Long studentId, Model model) {
    Grade grade = gradeService.findById(id);
    model.addAttribute("grade", grade);
    model.addAttribute("students", studentService.findAll());
    model.addAttribute("subjects", subjectService.findAll());
    model.addAttribute("studentId", studentId);
    if (studentId != null) {
      model.addAttribute("selectedStudent", studentService.findById(studentId));
    }
    return "admin/grades/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("grade") Grade grade,
      BindingResult result, @RequestParam(required = false) Long studentId, Model model) {
    if (result.hasErrors()) {
      model.addAttribute("students", studentService.findAll());
      model.addAttribute("subjects", subjectService.findAll());
      model.addAttribute("studentId", studentId);
      if (studentId != null) {
        model.addAttribute("selectedStudent", studentService.findById(studentId));
      }
      return "admin/grades/form";
    }
    grade.setId(id);
    gradeService.save(grade);
    if (studentId != null) return "redirect:/admin/students/" + studentId + "/bulletin";
    return "redirect:/admin/grades";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, @RequestParam(required = false) Long studentId) {
    gradeService.deleteById(id);
    if (studentId != null) return "redirect:/admin/students/" + studentId + "/bulletin";
    return "redirect:/admin/grades";
  }
}
