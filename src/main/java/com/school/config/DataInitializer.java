package com.school.config;

import com.school.entity.*;
import com.school.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Configuration
public class DataInitializer {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    // Injection par constructeur (plus propre et évite les erreurs de Bean)
    public DataInitializer(UserRepository userRepository, StudentRepository studentRepository,
                           ProfessorRepository professorRepository, SubjectRepository subjectRepository,
                           GradeRepository gradeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.subjectRepository = subjectRepository;
        this.gradeRepository = gradeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> runInitialization();
    }

    @Transactional
    public void runInitialization() {
        // 1. Admin
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new User("admin", passwordEncoder.encode("adminEAV"), Role.ADMIN));
        }

        // 2. Professor
        Professor prof = professorRepository.findAll().stream().findFirst().orElseGet(() -> 
            professorRepository.save(new Professor("Jean", "Dupont", "jean.dupont@school.com"))
        );

        if (!userRepository.existsByUsername("prof1")) {
            User pu = new User("prof1", passwordEncoder.encode("prof123"), Role.PROFESSOR);
            pu.setProfessor(prof);
            userRepository.save(pu);
        }

        // 3. Subjects
        Subject math = subjectRepository.findAll().stream()
                .filter(s -> "Math".equalsIgnoreCase(s.getName())).findFirst().orElseGet(() -> {
                    Subject s = new Subject("Math", 2);
                    s.setProfessor(prof);
                    return subjectRepository.save(s);
                });

        Subject francais = subjectRepository.findAll().stream()
                .filter(s -> "Francais".equalsIgnoreCase(s.getName())).findFirst().orElseGet(() -> {
                    Subject s = new Subject("Francais", 1);
                    s.setProfessor(prof);
                    return subjectRepository.save(s);
                });

        Subject anglais = subjectRepository.findAll().stream()
                .filter(s -> "Anglais".equalsIgnoreCase(s.getName())).findFirst().orElseGet(() -> 
                    subjectRepository.save(new Subject("Anglais", 1))
                );

        // 4. Student
        Optional<User> existingStudentUser = userRepository.findByUsername("Etudiant");

        if (existingStudentUser.isEmpty()) {
            Student st = new Student("Marie", "Paul", "marie.paul@student.com", "L1");
            st.setSecretCode("1234");
            st = studentRepository.save(st);

            User u = new User("Etudiant", passwordEncoder.encode("student"), Role.STUDENT);
            u.setStudent(st);
            userRepository.save(u);

            if (gradeRepository.findByStudentId(st.getId()).isEmpty()) {
                gradeRepository.save(new Grade(15.5, st, math));
                gradeRepository.save(new Grade(12.0, st, francais));
                gradeRepository.save(new Grade(14.0, st, anglais));
            }
        } else {
    studentRepository.findAll().forEach(st -> {
        if (st.getSecretCode() == null) {
            st.setSecretCode("1234");
            studentRepository.save(st);
        }
    });
  }      
}
}
