package com.school.controller;

import com.school.entity.Student;
import com.school.service.BulletinPdfService;
import com.school.service.GradeService;
import com.school.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/students")
public class StudentController {

  private final StudentService studentService;
  private final GradeService gradeService;
  private final BulletinPdfService bulletinPdfService;

  public StudentController(StudentService studentService, GradeService gradeService, BulletinPdfService bulletinPdfService) {
    this.studentService = studentService;
    this.gradeService = gradeService;
    this.bulletinPdfService = bulletinPdfService;
  }

  @GetMapping
  public String list(Model model) {
    var students = studentService.findAll();
    students.sort(java.util.Comparator.comparing(Student::getClassroom).thenComparing(Student::getLastName));
    model.addAttribute("students", students);
    return "admin/students/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("student", new Student());
    return "admin/students/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("student") Student student, BindingResult result, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) return "admin/students/form";
    var creds = studentService.createWithAccount(student);
    redirectAttributes.addFlashAttribute("createdAccount", creds);
    return "redirect:/admin/students";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("student", studentService.findById(id));
    return "admin/students/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("student") Student student, BindingResult result) {
    if (result.hasErrors()) return "admin/students/form";
    student.setId(id);
    studentService.save(student);
    return "redirect:/admin/students";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    studentService.deleteById(id);
    return "redirect:/admin/students";
  }

  @GetMapping("/{id}/bulletin")
  public String bulletin(@PathVariable Long id, Model model) {
    var student = studentService.findById(id);
    var grades = gradeService.findByStudentIdSorted(id);
    double weightedAverage = GradeService.computeWeightedAverage(grades);
    model.addAttribute("student", student);
    model.addAttribute("grades", grades);
    model.addAttribute("weightedAverage", weightedAverage);
    model.addAttribute("pdfUrl", "/admin/students/" + id + "/bulletin/pdf");
    model.addAttribute("backUrl", "/admin/bulletins");
    return "admin/students/bulletin";
  }

  @GetMapping("/{id}/bulletin/pdf")
  public ResponseEntity<byte[]> bulletinPdf(@PathVariable Long id) {
    byte[] pdf = bulletinPdfService.generateStudentBulletinPdf(id);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(ContentDisposition.inline().filename("bulletin-" + id + ".pdf").build());
    return ResponseEntity.ok().headers(headers).body(pdf);
  }
}
