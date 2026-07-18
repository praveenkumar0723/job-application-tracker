package com.praveen.jobtracker.repository;

import com.praveen.jobtracker.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.praveen.jobtracker.entity.User;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByCompanyContainingIgnoreCase(String company);

    List<Job> findByStatus(String status);

    long countByStatus(String status);

    List<Job> findAllByOrderByApplicationDateDesc();

    List<Job> findAllByOrderByApplicationDateAsc();

    // Pagination
    Page<Job> findAll(Pageable pageable);

    List<Job> findTop5ByInterviewDateIsNotNullOrderByInterviewDateAsc();

    List<Job> findByUser(User user);

    Page<Job> findByUser(User user, Pageable pageable);

    List<Job> findByUserAndCompanyContainingIgnoreCase(User user, String company);

    List<Job> findByUserAndStatus(User user, String status);

    long countByUser(User user);

    long countByUserAndStatus(User user, String status);

}