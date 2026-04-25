package com.school.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "subjects")
public class Subject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false, unique = true)
  private String name;

  @NotNull
  @Min(1)
  @Column(nullable = false)
  private Integer coefficient = 1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "professor_id")
  private Professor professor; // optional

  public Subject() {}

  public Subject(String name, Integer coefficient) {
    this.name = name;
    this.coefficient = coefficient;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Integer getCoefficient() { return coefficient; }
  public void setCoefficient(Integer coefficient) { this.coefficient = coefficient; }

  public Professor getProfessor() { return professor; }
  public void setProfessor(Professor professor) { this.professor = professor; }
}
