package com.school.controller;

import com.school.service.GradeService;
import com.school.service.StudentService;
import com.school.entity.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/admin/bulletins")
public class BulletinAdminController {

  private final StudentService studentService;
  private final GradeService gradeService;

  public BulletinAdminController(StudentService studentService, GradeService gradeService) {
    this.studentService = studentService;
    this.gradeService = gradeService;
  }

  @GetMapping
  public String list(Model model) {
    var students = studentService.findAll();
    students.sort(java.util.Comparator.comparing(Student::getClassroom).thenComparing(Student::getLastName));

    // Build summary per student
    List<Map<String, Object>> bulletins = new ArrayList<>();
    for (var st : students) {
      var grades = gradeService.findByStudentId(st.getId());
      double avg = GradeService.computeWeightedAverage(grades);
      Map<String, Object> row = new LinkedHashMap<>();
      row.put("student", st);
      row.put("noteCount", grades.size());
      row.put("average", avg);
      row.put("hasGrades", !grades.isEmpty());
      bulletins.add(row);
    }

    model.addAttribute("bulletins", bulletins);
    return "admin/bulletins/list";
  }

  @PostMapping("/{id}/delete")
  public String deleteBulletin(@PathVariable Long id) {
    gradeService.deleteByStudentId(id);
    return "redirect:/admin/bulletins";
  }
}
