package com.school.service.impl;

import com.school.entity.Subject;
import com.school.exception.NotFoundException;
import com.school.repository.SubjectRepository;
import com.school.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SubjectServiceImpl implements SubjectService {

  private final SubjectRepository repo;

  public SubjectServiceImpl(SubjectRepository repo) {
    this.repo = repo;
  }

  @Override
  public List<Subject> findAll() {
    return repo.findAllWithProfessor();
  }

  @Override
  public Subject findById(Long id) {
    return repo.findByIdWithProfessor(id).orElseThrow(() -> new NotFoundException("Subject not found: " + id));
  }

  @Override
  @Transactional
  public Subject save(Subject subject) {
    return repo.save(subject);
  }

  @Override
  public java.util.List<com.school.entity.Subject> findByProfessorId(Long professorId) {
    return repo.findByProfessorId(professorId);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    repo.deleteById(id);
  }
}
