package com.school.controller;

import com.school.entity.Role;
import com.school.service.SubjectService;
import com.school.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/professor")
public class ProfessorAreaController {

  private final UserService userService;
  private final SubjectService subjectService;

  public ProfessorAreaController(UserService userService, SubjectService subjectService) {
    this.userService = userService;
    this.subjectService = subjectService;
  }

  @GetMapping("/dashboard")
  public String dashboard(Authentication authentication, Model model) {
    var user = userService.findByUsername(authentication.getName());
    if (user.getRole() != Role.PROFESSOR || user.getProfessor() == null) {
      return "redirect:/post-login";
    }

    var prof = user.getProfessor();
    model.addAttribute("professor", prof);
    model.addAttribute("subjects", subjectService.findByProfessorId(prof.getId()));
    return "professor/dashboard";
  }
}
