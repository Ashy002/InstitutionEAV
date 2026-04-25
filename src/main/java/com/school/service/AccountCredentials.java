package com.school.service;

import com.school.entity.Role;

public class AccountCredentials {
  private final String username;
  private final String rawPassword;
  private final Role role;

  public AccountCredentials(String username, String rawPassword, Role role) {
    this.username = username;
    this.rawPassword = rawPassword;
    this.role = role;
  }

  public String getUsername() { return username; }
  public String getRawPassword() { return rawPassword; }
  public Role getRole() { return role; }
}
