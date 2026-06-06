package com.school.repository;

import com.school.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
  @Query("select s from Subject s left join fetch s.professor")
  List<Subject> findAllWithProfessor();

  @Query("select s from Subject s left join fetch s.professor where s.id = :id")
  java.util.Optional<Subject> findByIdWithProfessor(Long id);

  List<Subject> findByProfessorId(Long professorId);
}
