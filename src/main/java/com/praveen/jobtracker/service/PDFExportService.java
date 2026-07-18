package com.praveen.jobtracker.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.praveen.jobtracker.entity.Job;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PDFExportService {

    public ByteArrayInputStream export(List<Job> jobs) {

        Document document = new Document(PageSize.A4);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    18
            );

            Paragraph title = new Paragraph(
                    "Job Application Report",
                    titleFont
            );

            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(7);

            table.setWidthPercentage(100);

            table.setWidths(new int[]{
                    3, 3, 3, 2, 3, 3, 2
            });

            addHeader(table, "Company");
            addHeader(table, "Job Title");
            addHeader(table, "Location");
            addHeader(table, "Status");
            addHeader(table, "Applied");
            addHeader(table, "Interview");
            addHeader(table, "Salary");

            for (Job job : jobs) {

                table.addCell(job.getCompany());

                table.addCell(job.getJobTitle());

                table.addCell(job.getLocation());

                table.addCell(job.getStatus());

                table.addCell(
                        job.getApplicationDate() != null ?
                                job.getApplicationDate().toString() : ""
                );

                table.addCell(
                        job.getInterviewDate() != null ?
                                job.getInterviewDate().toString() : ""
                );

                table.addCell(
                        job.getSalary() != null ?
                                job.getSalary() : ""
                );
            }

            document.add(table);

            document.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addHeader(PdfPTable table, String text) {

        PdfPCell cell = new PdfPCell();

        cell.setPhrase(new Phrase(text));

        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(cell);
    }

}