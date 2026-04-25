package com.school.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String passwordHash;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "student_id")
  private Student student; // only for STUDENT role

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "professor_id")
  private Professor professor; // only for PROFESSOR role

  public User() {}

  public User(String username, String passwordHash, Role role) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.role = role;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }

  public Student getStudent() { return student; }
  public void setStudent(Student student) { this.student = student; }

  public Professor getProfessor() { return professor; }
  public void setProfessor(Professor professor) { this.professor = professor; }

} 
