package com.school.service;

import com.school.entity.Professor;

import java.util.List;

public interface ProfessorService {
  List<Professor> findAll();
  Professor findById(Long id);
  Professor save(Professor professor);

  AccountCredentials createWithAccount(Professor professor);
  void deleteById(Long id);
}
