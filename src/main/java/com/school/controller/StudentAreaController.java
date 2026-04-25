package com.school.controller;

import com.school.entity.Role;
import com.school.entity.Student;
import com.school.service.BulletinPdfService;
import com.school.service.GradeService;
import com.school.service.StudentService;
import com.school.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentAreaController {

  private final UserService userService;
  private final GradeService gradeService;
  private final BulletinPdfService bulletinPdfService;
  private final StudentService studentService;

  public StudentAreaController(UserService userService, GradeService gradeService,
      BulletinPdfService bulletinPdfService, StudentService studentService) {
    this.userService = userService;
    this.gradeService = gradeService;
    this.bulletinPdfService = bulletinPdfService;
    this.studentService = studentService;
  }

  @GetMapping("/dashboard")
  public String dashboard(Authentication authentication, Model model,
      @RequestParam(required = false) String error) {

    var user = userService.findByUsername(authentication.getName());
    if (user.getRole() != Role.STUDENT || user.getStudent() == null) {
      return "redirect:/post-login";
    }

    // Tous les étudiants triés par classe puis par nom
    List<Student> allStudents = studentService.findAll();
    allStudents.sort(Comparator.comparing(Student::getClassroom)
        .thenComparing(Student::getLastName));

    model.addAttribute("allStudents", allStudents);

    if (error != null) {
      model.addAttribute("errorMessage", "Code incorrect. Veuillez réessayer.");
    }
    return "student/dashboard";
  }

  // L'étudiant soumet le code pour voir LE bulletin de l'étudiant choisi (studentId)
  @PostMapping("/bulletin")
  public String bulletin(@RequestParam Long studentId,
      @RequestParam String code,
      Model model, HttpSession session) {

    // Charger l'étudiant dont on veut voir le bulletin
    Student student = studentService.findById(studentId);

    // Vérifier le code secret de CET étudiant
    if (student.getSecretCode() == null || !student.getSecretCode().equals(code)) {
      return "redirect:/student/dashboard?error=true";
    }

    session.setAttribute("bulletin_unlocked_" + student.getId(), true);

    var grades = gradeService.findByStudentIdSorted(student.getId());
    double weightedAverage = GradeService.computeWeightedAverage(grades);

    model.addAttribute("student", student);
    model.addAttribute("grades", grades);
    model.addAttribute("weightedAverage", weightedAverage);
    model.addAttribute("pdfUrl", "/student/bulletin/pdf?studentId=" + student.getId());
    model.addAttribute("backUrl", "/student/dashboard");
    return "student/bulletin";
  }

  @GetMapping("/bulletin/pdf")
  public ResponseEntity<byte[]> bulletinPdf(@RequestParam Long studentId, HttpSession session) {
    Boolean unlocked = (Boolean) session.getAttribute("bulletin_unlocked_" + studentId);
    if (unlocked == null || !unlocked) return ResponseEntity.status(403).build();

    byte[] pdf = bulletinPdfService.generateStudentBulletinPdf(studentId);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(
        ContentDisposition.inline().filename("bulletin-" + studentId + ".pdf").build());
    return ResponseEntity.ok().headers(headers).body(pdf);
  }
}
