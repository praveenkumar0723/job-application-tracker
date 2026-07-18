package com.praveen.jobtracker.service;

import com.praveen.jobtracker.entity.Job;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public ByteArrayInputStream exportJobs(List<Job> jobs) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Jobs");

        // Header Style
        CellStyle headerStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Header Row
        Row header = sheet.createRow(0);

        String[] columns = {
                "Company",
                "Job Title",
                "Location",
                "Status",
                "Application Date",
                "Interview Date",
                "Salary"
        };

        for (int i = 0; i < columns.length; i++) {

            Cell cell = header.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data
        int rowNum = 1;

        for (Job job : jobs) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(job.getCompany());
            row.createCell(1).setCellValue(job.getJobTitle());
            row.createCell(2).setCellValue(job.getLocation());
            row.createCell(3).setCellValue(job.getStatus());

            row.createCell(4).setCellValue(
                    job.getApplicationDate() != null ?
                            job.getApplicationDate().toString() : ""
            );

            row.createCell(5).setCellValue(
                    job.getInterviewDate() != null ?
                            job.getInterviewDate().toString() : ""
            );

            row.createCell(6).setCellValue(
                    job.getSalary() != null ?
                            job.getSalary().toString() : ""
            );
        }

        // Auto Size Columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}