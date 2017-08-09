package com.agyletime.rostering.rest.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.agyletime.rostering.rest.model.Job;

@Repository
public interface JobRepository extends PagingAndSortingRepository<Job, Long> {
	List<Job> findByStartDate(Date date, Pageable pageable);

	List<Job> findByEndDate(Date date, Pageable pageable);

	List<Job> findByStatus(Job.Status status, Pageable pageable);

	List<Job> findByName(String name, Pageable pageable);

}
