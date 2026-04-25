package com.school.service.impl;

import com.school.service.BulletinPdfService;
import com.school.service.GradeService;
import com.school.service.StudentService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class BulletinPdfServiceImpl implements BulletinPdfService {

  private final StudentService studentService;
  private final GradeService gradeService;

  public BulletinPdfServiceImpl(StudentService studentService, GradeService gradeService) {
    this.studentService = studentService;
    this.gradeService = gradeService;
  }

  @Override
  public byte[] generateStudentBulletinPdf(Long studentId) {
    var student = studentService.findById(studentId);
    var grades = gradeService.findByStudentId(studentId);

    double sum = 0.0;
    double coefSum = 0.0;
    for (var g : grades) {
      int coef = (g.getSubject() != null && g.getSubject().getCoefficient() != null) ? g.getSubject().getCoefficient() : 1;
      sum += g.getValue() * coef;
      coefSum += coef;
    }
    double avg = coefSum == 0.0 ? 0.0 : sum / coefSum;

    try (PDDocument doc = new PDDocument(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      PDPage page = new PDPage(PDRectangle.A4);
      doc.addPage(page);

      try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
        float margin = 50;
        float y = page.getMediaBox().getHeight() - margin;

        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 18);
        cs.newLineAtOffset(margin, y);
        cs.showText("Bulletin - " + student.getFirstName() + " " + student.getLastName());
        cs.endText();

        y -= 30;
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA, 12);
        cs.newLineAtOffset(margin, y);
        cs.showText("Email: " + student.getEmail() + "   Classe: " + student.getClassroom());
        cs.endText();

        y -= 30;
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cs.newLineAtOffset(margin, y);
        cs.showText("Matiere");
        cs.newLineAtOffset(250, 0);
        cs.showText("Coef");
        cs.newLineAtOffset(60, 0);
        cs.showText("Note");
        cs.endText();

        y -= 15;
        cs.setLineWidth(1);
        cs.moveTo(margin, y);
        cs.lineTo(page.getMediaBox().getWidth() - margin, y);
        cs.stroke();

        y -= 20;
        cs.setFont(PDType1Font.HELVETICA, 12);

        for (var g : grades) {
          if (y < 100) break; // simple: 1 page for demo
          String subject = g.getSubject() != null ? g.getSubject().getName() : "-";
          int coef = (g.getSubject() != null && g.getSubject().getCoefficient() != null) ? g.getSubject().getCoefficient() : 1;

          cs.beginText();
          cs.newLineAtOffset(margin, y);
          cs.showText(subject);
          cs.newLineAtOffset(250, 0);
          cs.showText(String.valueOf(coef));
          cs.newLineAtOffset(60, 0);
          cs.showText(String.valueOf(g.getValue()));
          cs.endText();

          y -= 18;
        }

        y -= 10;
        cs.beginText();
        cs.setFont(PDType1Font.HELVETICA_BOLD, 12);
        cs.newLineAtOffset(margin, y);
        cs.showText("Moyenne ponderee: " + String.format("%.2f", avg));
        cs.endText();
      }

      doc.save(baos);
      return baos.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("PDF generation failed: " + e.getMessage(), e);
    }
  }
}
