package com.school.controller;

import com.school.entity.Grade;
import com.school.entity.Role;
import com.school.entity.Professor;
import com.school.service.GradeService;
import com.school.service.StatsService;
import com.school.service.StudentService;
import com.school.service.SubjectService;
import com.school.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/professor")
@Transactional(readOnly = true)
public class ProfessorAreaController {

  private final UserService userService;
  private final SubjectService subjectService;
  private final GradeService gradeService;
  private final StudentService studentService;
  private final StatsService statsService;

  public ProfessorAreaController(UserService userService, SubjectService subjectService,
      GradeService gradeService, StudentService studentService, StatsService statsService) {
    this.userService = userService;
    this.subjectService = subjectService;
    this.gradeService = gradeService;
    this.studentService = studentService;
    this.statsService = statsService;
  }

  private Professor getProfessor(Authentication auth) {
    var user = userService.findByUsername(auth.getName());
    if (user.getRole() != Role.PROFESSOR || user.getProfessor() == null)
      throw new IllegalStateException("Compte professeur non configuré");
    return user.getProfessor();
  }

  // ── Dashboard ──────────────────────────────────────────────────────────────
  @GetMapping("/dashboard")
  public String dashboard(Authentication auth, Model model) {
    var prof = getProfessor(auth);
    var subjects = subjectService.findByProfessorId(prof.getId());
    var allGrades = gradeService.findAll();
    long myGradeCount = allGrades.stream()
        .filter(g -> g.getSubject() != null && subjects.stream()
            .anyMatch(s -> s.getId().equals(g.getSubject().getId())))
        .count();

    model.addAttribute("professor", prof);
    model.addAttribute("subjects", subjects);
    model.addAttribute("gradeCount", myGradeCount);
    model.addAttribute("studentCount", studentService.findAll().size());
    return "professor/dashboard";
  }

  // ── Notes ──────────────────────────────────────────────────────────────────
  @GetMapping("/grades")
  public String gradeList(Authentication auth, Model model) {
    var prof = getProfessor(auth);
    var mySubjectIds = subjectService.findByProfessorId(prof.getId())
        .stream().map(s -> s.getId()).toList();
    var grades = gradeService.findAll().stream()
        .filter(g -> g.getSubject() != null && mySubjectIds.contains(g.getSubject().getId()))
        .toList();
    model.addAttribute("grades", grades);
    return "professor/grades/list";
  }

  @GetMapping("/grades/new")
  public String gradeForm(Authentication auth,
      @RequestParam(required = false) Long studentId, Model model) {
    var prof = getProfessor(auth);
    model.addAttribute("grade", new Grade());
    model.addAttribute("students", studentService.findAll());
    model.addAttribute("subjects", subjectService.findByProfessorId(prof.getId()));
    model.addAttribute("studentId", studentId);
    if (studentId != null)
      model.addAttribute("selectedStudent", studentService.findById(studentId));
    return "professor/grades/form";
  }

  @PostMapping("/grades")
  @Transactional
  public String gradeCreate(Authentication auth,
      @Valid @ModelAttribute("grade") Grade grade, BindingResult result,
      @RequestParam(required = false) Long studentId, Model model) {
    var prof = getProfessor(auth);
    if (result.hasErrors()) {
      model.addAttribute("students", studentService.findAll());
      model.addAttribute("subjects", subjectService.findByProfessorId(prof.getId()));
      model.addAttribute("studentId", studentId);
      if (studentId != null)
        model.addAttribute("selectedStudent", studentService.findById(studentId));
      return "professor/grades/form";
    }
    gradeService.save(grade);
    return "redirect:/professor/grades";
  }

  @GetMapping("/grades/{id}/edit")
  public String gradeEditForm(Authentication auth, @PathVariable Long id,
      @RequestParam(required = false) Long studentId, Model model) {
    var prof = getProfessor(auth);
    model.addAttribute("grade", gradeService.findById(id));
    model.addAttribute("students", studentService.findAll());
    model.addAttribute("subjects", subjectService.findByProfessorId(prof.getId()));
    model.addAttribute("studentId", studentId);
    if (studentId != null)
      model.addAttribute("selectedStudent", studentService.findById(studentId));
    return "professor/grades/form";
  }

  @PostMapping("/grades/{id}")
  @Transactional
  public String gradeUpdate(Authentication auth, @PathVariable Long id,
      @Valid @ModelAttribute("grade") Grade grade, BindingResult result,
      @RequestParam(required = false) Long studentId, Model model) {
    var prof = getProfessor(auth);
    if (result.hasErrors()) {
      model.addAttribute("students", studentService.findAll());
      model.addAttribute("subjects", subjectService.findByProfessorId(prof.getId()));
      model.addAttribute("studentId", studentId);
      if (studentId != null)
        model.addAttribute("selectedStudent", studentService.findById(studentId));
      return "professor/grades/form";
    }
    grade.setId(id);
    gradeService.save(grade);
    return "redirect:/professor/grades";
  }

  @PostMapping("/grades/{id}/delete")
  @Transactional
  public String gradeDelete(@PathVariable Long id) {
    gradeService.deleteById(id);
    return "redirect:/professor/grades";
  }

  // ── Matières (lecture seule) ───────────────────────────────────────────────
  @GetMapping("/subjects")
  public String subjectList(Authentication auth, Model model) {
    var prof = getProfessor(auth);
    model.addAttribute("subjects", subjectService.findByProfessorId(prof.getId()));
    return "professor/subjects";
  }

  // ── Statistiques ──────────────────────────────────────────────────────────
  @GetMapping("/stats")
  public String stats(Model model) {
    model.addAttribute("globalAverage", statsService.globalWeightedAverage());
    model.addAttribute("classroomAverages", statsService.averagesByClassroom());
    model.addAttribute("subjectAverages", statsService.averagesBySubject());
    model.addAttribute("studentAverages", statsService.averagesByStudent());
    return "professor/stats";
  }
}
