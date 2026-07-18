package com.praveen.jobtracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.praveen.jobtracker.entity.User;

@Entity
@Table(name = "jobs")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;

    private String jobTitle;

    private String location;

    private String salary;

    private LocalDate applicationDate;

    private LocalDate interviewDate;

    private String status;

    @Column(length = 1000)
    private String notes;

    @Column(name = "resume_file")
    private String resumeFile;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Job() {
    }

    public Job(Long id, String company, String jobTitle, String location,
               String salary, LocalDate applicationDate,
               LocalDate interviewDate, String status,
               String notes, String resumeFile) {

        this.id = id;
        this.company = company;
        this.jobTitle = jobTitle;
        this.location = location;
        this.salary = salary;
        this.applicationDate = applicationDate;
        this.interviewDate = interviewDate;
        this.status = status;
        this.notes = notes;
        this.resumeFile = resumeFile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDate getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(LocalDate interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(String resumeFile) {
        this.resumeFile = resumeFile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}