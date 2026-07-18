package com.praveen.jobtracker.controller;

import com.praveen.jobtracker.entity.Job;
import com.praveen.jobtracker.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.security.Principal;
import com.praveen.jobtracker.service.ExcelExportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.praveen.jobtracker.service.PDFExportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private ExcelExportService excelExportService;

    @Autowired
    private PDFExportService pdfExportService;

    // View All Jobs with Pagination
    @GetMapping("/jobs")
    public String viewJobs(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           Principal principal) {

        Page<Job> jobPage = jobService.getAllJobs(PageRequest.of(page, 5));

        model.addAttribute("jobPage", jobPage);
        model.addAttribute("jobs", jobPage.getContent());

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", jobPage.getTotalPages());

        model.addAttribute("totalJobs", jobService.getTotalJobs());
        model.addAttribute("appliedCount", jobService.getCountByStatus("Applied"));
        model.addAttribute("interviewCount", jobService.getCountByStatus("Interview"));
        model.addAttribute("selectedCount", jobService.getCountByStatus("Selected"));
        model.addAttribute("rejectedCount", jobService.getCountByStatus("Rejected"));

        model.addAttribute("username", principal.getName());

        return "jobs";
    }

    // Search Jobs
    @GetMapping("/jobs/search")
    public String searchJobs(@RequestParam String company,
                             Model model,
                             Principal principal) {

        model.addAttribute("jobs", jobService.searchJobs(company));

        model.addAttribute("totalJobs", jobService.getTotalJobs());
        model.addAttribute("appliedCount", jobService.getCountByStatus("Applied"));
        model.addAttribute("interviewCount", jobService.getCountByStatus("Interview"));
        model.addAttribute("selectedCount", jobService.getCountByStatus("Selected"));
        model.addAttribute("rejectedCount", jobService.getCountByStatus("Rejected"));

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);

        model.addAttribute("username", principal.getName());

        return "jobs";
    }

    // Add Job Page
    @GetMapping("/jobs/add")
    public String showAddJobPage(Model model) {

        model.addAttribute("job", new Job());

        return "add-job";
    }

    // Save Job
    @PostMapping("/jobs/save")
    public String saveJob(@Valid @ModelAttribute("job") Job job,
                          BindingResult result,
                          @RequestParam("resume") MultipartFile resume,
                          RedirectAttributes redirectAttributes) throws IOException {

        if (result.hasErrors()) {
            return "add-job";
        }

        if (job.getApplicationDate() == null) {
            job.setApplicationDate(LocalDate.now());
        }

        // Save Resume
        if (!resume.isEmpty()) {

            String uploadDir = "uploads/";

            Files.createDirectories(Paths.get(uploadDir));

            String fileName = System.currentTimeMillis() + "_" + resume.getOriginalFilename();

            Path path = Paths.get(uploadDir + fileName);

            Files.copy(resume.getInputStream(),
                    path,
                    StandardCopyOption.REPLACE_EXISTING);

            job.setResumeFile(fileName);
        }

        boolean isNew = (job.getId() == null);

        jobService.saveJob(job);

        String message = isNew
                ? "Job added successfully!"
                : "Job updated successfully!";

        redirectAttributes.addFlashAttribute("successMessage", message);

        return "redirect:/jobs";
    }

    // Edit Job
    @GetMapping("/jobs/edit/{id}")
    public String editJob(@PathVariable Long id, Model model) {

        Job job = jobService.getJobById(id).orElseThrow();

        model.addAttribute("job", job);

        return "add-job";
    }

    // Delete Job
    @GetMapping("/jobs/delete/{id}")
    public String deleteJob(@PathVariable Long id,
                            RedirectAttributes redirectAttributes) {

        jobService.deleteJob(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Job deleted successfully!"
        );

        return "redirect:/jobs";
    }

    // Filter Jobs
    @GetMapping("/jobs/filter")
    public String filterJobs(@RequestParam String status,
                             Model model,
                             Principal principal) {

        model.addAttribute("jobs", jobService.filterJobs(status));

        model.addAttribute("totalJobs", jobService.getTotalJobs());
        model.addAttribute("appliedCount", jobService.getCountByStatus("Applied"));
        model.addAttribute("interviewCount", jobService.getCountByStatus("Interview"));
        model.addAttribute("selectedCount", jobService.getCountByStatus("Selected"));
        model.addAttribute("rejectedCount", jobService.getCountByStatus("Rejected"));

        model.addAttribute("upcomingInterviews",
                jobService.getUpcomingInterviews());

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);

        model.addAttribute("username", principal.getName());

        return "jobs";
    }

    // Sort Jobs
    @GetMapping("/jobs/sort")
    public String sortJobs(@RequestParam String field,
                           Model model,
                           Principal principal) {

        model.addAttribute("jobs", jobService.getAllJobs(Sort.by(field)));

        model.addAttribute("totalJobs", jobService.getTotalJobs());
        model.addAttribute("appliedCount", jobService.getCountByStatus("Applied"));
        model.addAttribute("interviewCount", jobService.getCountByStatus("Interview"));
        model.addAttribute("selectedCount", jobService.getCountByStatus("Selected"));
        model.addAttribute("rejectedCount", jobService.getCountByStatus("Rejected"));

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);

        model.addAttribute("username", principal.getName());

        return "jobs";
    }
    @GetMapping("/jobs/newest")
    public String newestJobs(Model model, Principal principal) {

        model.addAttribute("jobs", jobService.getNewestJobs());

        model.addAttribute("totalJobs", jobService.getTotalJobs());
        model.addAttribute("appliedCount", jobService.getCountByStatus("Applied"));
        model.addAttribute("interviewCount", jobService.getCountByStatus("Interview"));
        model.addAttribute("selectedCount", jobService.getCountByStatus("Selected"));
        model.addAttribute("rejectedCount", jobService.getCountByStatus("Rejected"));

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("username", principal.getName());

        return "jobs";
    }

    @GetMapping("/jobs/oldest")
    public String oldestJobs(Model model, Principal principal) {

        model.addAttribute("jobs", jobService.getOldestJobs());

        model.addAttribute("totalJobs", jobService.getTotalJobs());
        model.addAttribute("appliedCount", jobService.getCountByStatus("Applied"));
        model.addAttribute("interviewCount", jobService.getCountByStatus("Interview"));
        model.addAttribute("selectedCount", jobService.getCountByStatus("Selected"));
        model.addAttribute("rejectedCount", jobService.getCountByStatus("Rejected"));

        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("username", principal.getName());

        return "jobs";
    }

    @GetMapping("/jobs/export")
    public ResponseEntity<InputStreamResource> exportExcel() throws IOException {

        InputStreamResource file = new InputStreamResource(
                excelExportService.exportJobs(jobService.getAllJobs())
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=JobTracker.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @GetMapping("/jobs/export/pdf")
    public ResponseEntity<InputStreamResource> exportPdf() throws IOException {

        InputStreamResource file = new InputStreamResource(
                pdfExportService.export(jobService.getAllJobs())
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=JobTracker.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(file);
    }

    @GetMapping("/jobs/download/{file}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String file)
            throws MalformedURLException {

        Path path = Paths.get("uploads").resolve(file);

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}