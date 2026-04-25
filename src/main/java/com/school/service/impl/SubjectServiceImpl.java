package com.school.service.impl;

import com.school.entity.Subject;
import com.school.exception.NotFoundException;
import com.school.repository.SubjectRepository;
import com.school.service.SubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

  private final SubjectRepository repo;

  public SubjectServiceImpl(SubjectRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Subject> findAll() {
    return repo.findAll();
  }

  @Override
  public Subject findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Subject not found: " + id));
  }

  @Override
  public Subject save(Subject subject) {
    return repo.save(subject);
  }

  @Override
  public java.util.List<com.school.entity.Subject> findByProfessorId(Long professorId) {
    return repo.findByProfessorId(professorId);
  }

  @Override
  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
