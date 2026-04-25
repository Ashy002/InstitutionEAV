package com.school.service;

import java.util.List;

public interface StatsService {

  record SubjectAverage(Long subjectId, String subjectName, double average, long count) {}
  record StudentAverage(Long studentId, String studentName, String classroom, double weightedAverage) {}

  double globalWeightedAverage();
  List<SubjectAverage> averagesBySubject();
  List<StudentAverage> averagesByStudent();
}
