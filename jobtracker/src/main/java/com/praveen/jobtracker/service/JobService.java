package com.praveen.jobtracker.service;

import com.praveen.jobtracker.entity.Job;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface JobService {

    List<Job> getAllJobs();

    void saveJob(Job job);

    Optional<Job> getJobById(Long id);

    void deleteJob(Long id);

    List<Job> searchJobs(String company);

    List<Job> filterJobs(String status);   // ← Add this

    long getTotalJobs();

    long getCountByStatus(String status);

    Page<Job> getAllJobs(Pageable pageable);

    List<Job> getAllJobs(Sort sort);

    List<Job> getNewestJobs();

    List<Job> getOldestJobs();

    List<Job> getUpcomingInterviews();
}