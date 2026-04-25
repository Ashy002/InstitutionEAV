package com.school.service.impl;

import com.school.entity.User;
import com.school.exception.NotFoundException;
import com.school.repository.UserRepository;
import com.school.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository repo;

  public UserServiceImpl(UserRepository repo) {
    this.repo = repo;
  }

  @Override
  public User findByUsername(String username) {
    return repo.findByUsername(username)
        .orElseThrow(() -> new NotFoundException("User not found: " + username));
  }

  @Override
  public User save(User user) {
    return repo.save(user);
  }

  @Override
  public boolean existsByUsername(String username) {
    return repo.existsByUsername(username);
  }
}
