package com.praveen.jobtracker.service.impl;

import com.praveen.jobtracker.entity.Job;
import com.praveen.jobtracker.repository.JobRepository;
import com.praveen.jobtracker.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public void saveJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    @Override
    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    @Override
    public List<Job> searchJobs(String company) {
        return jobRepository.findByCompanyContainingIgnoreCase(company);
    }
    @Override
    public List<Job> filterJobs(String status) {
        return jobRepository.findByStatus(status);
    }

    @Override
    public long getTotalJobs() {
        return jobRepository.count();
    }

    @Override
    public long getCountByStatus(String status) {
        return jobRepository.countByStatus(status);
    }
    @Override
    public Page<Job> getAllJobs(Pageable pageable) {
        return jobRepository.findAll(pageable);
    }
    @Override
    public List<Job> getAllJobs(Sort sort) {
        return jobRepository.findAll(sort);
    }

    @Override
    public List<Job> getNewestJobs() {
        return jobRepository.findAllByOrderByApplicationDateDesc();
    }

    @Override
    public List<Job> getOldestJobs() {
        return jobRepository.findAllByOrderByApplicationDateAsc();
    }

    @Override
    public List<Job> getUpcomingInterviews() {
        return jobRepository.findTop5ByInterviewDateIsNotNullOrderByInterviewDateAsc();
    }


}