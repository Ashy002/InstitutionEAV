package com.school.controller;

import com.school.service.StatsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/stats")
@Transactional(readOnly = true)
public class StatsController {

  private final StatsService statsService;

  public StatsController(StatsService statsService) {
    this.statsService = statsService;
  }

  @GetMapping
  public String stats(Model model) {
    model.addAttribute("globalAverage", statsService.globalWeightedAverage());
    model.addAttribute("classroomAverages", statsService.averagesByClassroom());
    model.addAttribute("subjectAverages", statsService.averagesBySubject());
    model.addAttribute("studentAverages", statsService.averagesByStudent());
    return "admin/stats";
  }
}
