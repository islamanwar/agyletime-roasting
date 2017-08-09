package com.agyletime.rostering.rest.response;

import java.util.List;

import com.agyletime.rostering.rest.model.Job;

public class JobResponse extends BaseResponse{

	private Job job;

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public JobResponse(Job job) {
		super();
		this.job = job;
	}


	
	
}
