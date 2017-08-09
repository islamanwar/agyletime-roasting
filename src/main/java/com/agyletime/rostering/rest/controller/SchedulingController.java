package com.agyletime.rostering.rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agyletime.rostering.model.ShiftComposition;
import com.agyletime.rostering.rest.custom.JobManager;
import com.agyletime.rostering.rest.model.Job;
import com.agyletime.rostering.rest.model.Job.Status;
import com.agyletime.rostering.rest.repository.JobRepository;
import com.agyletime.rostering.rest.response.BaseResponse;
import com.agyletime.rostering.rest.response.JobResponse;
import com.agyletime.rostering.rest.response.JobsResponse;
import com.agyletime.rostering.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

@RestController
@RequestMapping(path = "optimise")
public class SchedulingController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(SchedulingController.class);

	@Autowired
	private JobManager jobManager;

	@Autowired
	private JobRepository repository;

	private static final Gson sGson = new GsonBuilder().create();

	/**
	 * Implies the service is up and running
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, path = "ping", produces = "application/json")
	public ResponseEntity<? extends BaseResponse> ping() {

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setMessage("Service is up and running");
		return new ResponseEntity<BaseResponse>(baseResponse, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json" }, produces = { "application/json" })
	public ResponseEntity<? extends BaseResponse> schedule(@RequestParam(name = "callback", required = false) String callbackUri, @RequestBody(required = false) String body) {

		// Validate request body not empty
		if (StringUtils.isEmpty(body)) {
			return new ResponseEntity<BaseResponse>(
					new BaseResponse(HttpStatus.BAD_REQUEST.ordinal(), "Empty Request Body"), HttpStatus.BAD_REQUEST);
		}

		ShiftComposition shiftComposition;
		// Validate request body structure
		try {
			shiftComposition = sGson.fromJson(body, ShiftComposition.class);
		} catch (JsonSyntaxException ex) {
			LOGGER.error(ex.getMessage(), ex);
			return new ResponseEntity<BaseResponse>(
					new BaseResponse(HttpStatus.BAD_REQUEST.ordinal(), "Bad Request Body"), HttpStatus.BAD_REQUEST);
		}
		if(!StringUtils.isEmpty(callbackUri) && !Utils.validateUrl(callbackUri)) {
			return new ResponseEntity<BaseResponse>(
					new BaseResponse(HttpStatus.BAD_REQUEST.ordinal(), "Invalid Callback uri"), HttpStatus.BAD_REQUEST);
		}
		// Set task's date to the date sent with the request
		/*
		 * TODO set task date when results are available List<Task> tasks =
		 * shiftComposition.getTasks();
		 * 
		 * if (tasks != null && !tasks.isEmpty()) { for (Task task : tasks) {
		 * task.setDate(shiftComposition.getDate()); } }
		 */
		long jobId = -1;
		try {
			jobId = jobManager.submitJob(shiftComposition, callbackUri);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<BaseResponse>(new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.ordinal(),
					"An error occurred while submitting the job."), HttpStatus.INTERNAL_SERVER_ERROR);
		}

		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add("location", "jobs/" + jobId);

		ResponseEntity<BaseResponse> response = new ResponseEntity<BaseResponse>(
				new BaseResponse(HttpStatus.ACCEPTED.value(), "Job Accepted Successfully"), headers,
				HttpStatus.ACCEPTED);

		return response;
	}

	@RequestMapping(method = RequestMethod.GET, path = "queue/{jobId}", produces = "application/json")
	public ResponseEntity<? extends BaseResponse> getJob(@PathVariable long jobId) {

		if (jobId <= 0) {
			return new ResponseEntity<BaseResponse>(new BaseResponse(400, "Invalid Client Request", "Invalid Job Id"),
					HttpStatus.BAD_REQUEST);
		}
		Job job = new Job();
		try {
			job = repository.findOne(jobId);
			JobResponse jobResponse = new JobResponse(job);

			return new ResponseEntity<JobResponse>(jobResponse, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<BaseResponse>(
					new BaseResponse(HttpStatus.OK.value(), "Job Not Found", "Job Not Found"), HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.GET, path = "queue", produces = "application/json")
	public ResponseEntity<? extends BaseResponse> getJobs(
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "page", required = false) int page,
			@RequestParam(value = "size", required = false) int size) {

		Iterable<Job> jobs = new ArrayList<Job>();

		if (size <= 0 || size > 100)
			size = 10;

		if (page <= 0)
			page = 0;
		try {

			Pageable pageable = new PageRequest(page, size);
			if (StringUtils.isEmpty(status)) {
				jobs = repository.findAll(pageable);
			} else {
				try {
					Job.Status jobStaus = Status.valueOf(status);
					jobs = repository.findByStatus(jobStaus, pageable);
				} catch (IllegalArgumentException ex) {
					return new ResponseEntity<BaseResponse>(new BaseResponse(400, "Invalid Client Request", "Invalid Job Status"), HttpStatus.BAD_REQUEST);
				}catch (Exception e) {
					return new ResponseEntity<BaseResponse>( new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server Error", "Invalid Job Status"), HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
		} catch (Exception e) {
			e.getMessage();
		}
		JobsResponse jobsResponse = new JobsResponse(jobs);

		return new ResponseEntity<JobsResponse>(jobsResponse, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.PUT, path = "queue/{jobId}")
	public ResponseEntity<? extends BaseResponse> cancelJob(@PathVariable long jobId) {
		// Valid job exists
		Job job = repository.findOne(jobId);
		if (job == null) {
			return new ResponseEntity<BaseResponse>(
					new BaseResponse(HttpStatus.OK.value(), "Job Not Found", "Job Not Found"), HttpStatus.OK);
		}
		// Validate job not already finished
		Future<?> future = JobManager.CURRRNT_JOBS.get(jobId);
		if (future == null || future.isDone())
			return new ResponseEntity<BaseResponse>(new BaseResponse(HttpStatus.OK.value(), "Job Already Finished."),
					HttpStatus.OK);

		try {
			// Cancel the running job
			future.cancel(true);

			// Update job status
			job.setStatus(Status.CANCELLED);
			job.setEndDate(new Date());
			job.setExtraDetails("Cancelled through API");

			repository.save(job);
			BaseResponse cancelResponse = new BaseResponse();
			return new ResponseEntity<BaseResponse>(cancelResponse, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return new ResponseEntity<BaseResponse>(
					new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error has occurred"),
					HttpStatus.INTERNAL_SERVER_ERROR);

		}

	}
}
