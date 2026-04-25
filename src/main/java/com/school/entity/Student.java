package com.school.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "students")
public class Student {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(nullable = false)
  private String firstName;

  @NotBlank
  @Column(nullable = false)
  private String lastName;

  @NotBlank
  @Email
  @Column(nullable = false, unique = true)
  private String email;

  @NotBlank
  @Column(nullable = false)
  private String classroom;

  private String secretCode;

  public Student() {}

  public Student(String firstName, String lastName, String email, String classroom) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.classroom = classroom;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getFirstName() { return firstName; }
  public void setFirstName(String firstName) { this.firstName = firstName; }

  public String getLastName() { return lastName; }
  public void setLastName(String lastName) { this.lastName = lastName; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getClassroom() { return classroom; }
  public void setClassroom(String classroom) { this.classroom = classroom; }

  public String getSecretCode() { return secretCode; }
  public void setSecretCode(String secretCode) { this.secretCode = secretCode; }
}
