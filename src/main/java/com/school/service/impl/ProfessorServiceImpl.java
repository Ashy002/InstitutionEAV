package com.school.service.impl;

import com.school.entity.Professor;
import com.school.entity.Role;
import com.school.entity.User;
import com.school.exception.NotFoundException;
import com.school.repository.ProfessorRepository;
import com.school.repository.UserRepository;
import com.school.service.AccountCredentials;
import com.school.service.ProfessorService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

  private final ProfessorRepository repo;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private static final SecureRandom rng = new SecureRandom();

  public ProfessorServiceImpl(ProfessorRepository repo, UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.repo = repo;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override public List<Professor> findAll() { return repo.findAll(); }

  @Override
  public Professor findById(Long id) {
    return repo.findById(id).orElseThrow(() -> new NotFoundException("Professeur introuvable: " + id));
  }

  @Override public Professor save(Professor professor) { return repo.save(professor); }

  @Override
  @Transactional
  public AccountCredentials createWithAccount(Professor professor) {
    Professor saved = repo.save(professor);
    String username = saved.getEmail().trim().toLowerCase();
    if (userRepository.existsByUsername(username)) {
      username = username + "." + saved.getId();
    }
    String rawPassword = generatePassword("Pr");
    User u = new User(username, passwordEncoder.encode(rawPassword), Role.PROFESSOR);
    u.setProfessor(saved);
    userRepository.save(u);
    return new AccountCredentials(username, rawPassword, Role.PROFESSOR);
  }

  private String generatePassword(String prefix) {
    String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789@#$%";
    StringBuilder sb = new StringBuilder();
    sb.append(prefix).append("@");
    for (int i = 0; i < 9; i++) sb.append(chars.charAt(rng.nextInt(chars.length())));
    return sb.toString();
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    userRepository.findByProfessorId(id).ifPresent(userRepository::delete);
    repo.deleteById(id);
  }
}
