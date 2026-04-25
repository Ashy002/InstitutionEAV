package com.school.service;

public interface BulletinPdfService {
  byte[] generateStudentBulletinPdf(Long studentId);
}
