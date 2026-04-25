package com.school.controller;

import com.school.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final StudentService studentService;
  private final ProfessorService professorService;
  private final SubjectService subjectService;
  private final StatsService statsService;
  private final GradeService gradeService;

  public AdminController(StudentService studentService, ProfessorService professorService,
      SubjectService subjectService, StatsService statsService, GradeService gradeService) {
    this.studentService = studentService;
    this.professorService = professorService;
    this.subjectService = subjectService;
    this.statsService = statsService;
    this.gradeService = gradeService;
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model) {
    model.addAttribute("studentsCount", studentService.findAll().size());
    model.addAttribute("professorsCount", professorService.findAll().size());
    model.addAttribute("subjectsCount", subjectService.findAll().size());
    model.addAttribute("globalAverage", statsService.globalWeightedAverage());
    return "admin/dashboard";
  }

}
