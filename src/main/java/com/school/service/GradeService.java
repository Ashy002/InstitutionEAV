package com.school.service;

import com.school.entity.Grade;
import java.util.List;

public interface GradeService {
  List<Grade> findAll();
  Grade findById(Long id);
  Grade save(Grade grade);
  void deleteById(Long id);
  List<Grade> findByStudentId(Long studentId);
  List<Grade> findByStudentIdSorted(Long studentId);
  void deleteByStudentId(Long studentId);

  static double computeWeightedAverage(List<Grade> grades) {
    double sum = 0.0, coefSum = 0.0;
    for (var g : grades) {
      int coef = (g.getSubject() != null && g.getSubject().getCoefficient() != null)
          ? g.getSubject().getCoefficient() : 1;
      sum += g.getValue() * coef;
      coefSum += coef;
    }
    return coefSum == 0.0 ? 0.0 : sum / coefSum;
  }
}
