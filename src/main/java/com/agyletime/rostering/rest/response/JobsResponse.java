package com.agyletime.rostering.rest.response;

import java.util.List;

import com.agyletime.rostering.rest.model.Job;

public class JobsResponse extends BaseResponse{

	private Iterable<Job> jobs;

	
	public JobsResponse(Iterable<Job> jobs) {
		super();
		this.jobs = jobs;
	}

	public Iterable<Job> getJobs() {
		return jobs;
	}

	public void setJobs(Iterable<Job> jobs) {
		this.jobs = jobs;
	}
	
	
	
}
