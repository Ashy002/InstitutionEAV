package com.school.service;

import com.school.entity.Subject;

import java.util.List;

public interface SubjectService {
  List<Subject> findAll();
  Subject findById(Long id);
  Subject save(Subject subject);
  void deleteById(Long id);

  List<Subject> findByProfessorId(Long professorId);
}
