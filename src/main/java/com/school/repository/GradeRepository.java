package com.school.repository;

import com.school.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
  @Query("select g from Grade g join fetch g.student join fetch g.subject")
  List<Grade> findAllWithStudentAndSubject();

  @Query("select g from Grade g join fetch g.student join fetch g.subject where g.id = :id")
  Optional<Grade> findByIdWithStudentAndSubject(Long id);

  @Query("select g from Grade g join fetch g.subject where g.student.id = :studentId order by g.subject.name")
  List<Grade> findByStudentIdOrderBySubjectNameAsc(Long studentId);

  @Query("select g from Grade g join fetch g.student join fetch g.subject where g.student.id = :studentId")
  List<Grade> findByStudentIdWithStudentAndSubject(Long studentId);

  @Query("select g from Grade g join fetch g.student join fetch g.subject where g.student.id = :studentId")
  List<Grade> findByStudentId(Long studentId);

  @Transactional
  void deleteByStudentId(Long studentId);
}
