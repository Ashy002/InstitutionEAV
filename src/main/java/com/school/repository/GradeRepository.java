package com.school.repository;

import com.school.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
  List<Grade> findByStudentId(Long studentId);
  List<Grade> findByStudentIdOrderBySubjectNameAsc(Long studentId);

  @Transactional
  void deleteByStudentId(Long studentId);
}
