package com.school.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "grades",
  uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id"})
)
public class Grade {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Min(0)
  @Max(20)
  @Column(nullable = false)
  private Double value;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id", nullable = false)
  private Student student;

  @NotNull
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "subject_id", nullable = false)
  private Subject subject;

  public Grade() {}

  public Grade(Double value, Student student, Subject subject) {
    this.value = value;
    this.student = student;
    this.subject = subject;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public Double getValue() { return value; }
  public void setValue(Double value) { this.value = value; }

  public Student getStudent() { return student; }
  public void setStudent(Student student) { this.student = student; }

  public Subject getSubject() { return subject; }
  public void setSubject(Subject subject) { this.subject = subject; }
}
