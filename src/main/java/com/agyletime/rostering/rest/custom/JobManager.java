package com.agyletime.rostering.rest.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.agyletime.rostering.model.ShiftComposition;
import com.agyletime.rostering.rest.controller.SchedulingController;
import com.agyletime.rostering.rest.model.Job;
import com.agyletime.rostering.rest.repository.JobRepository;

import com.google.gson.Gson;

@Component
public class JobManager {

	@Autowired
	private JobRepository repository;

	@Autowired
	private JobRunnable runnableJob;

	private ThreadPoolTaskExecutor taskExecutor;

	protected static final Logger LOGGER = LoggerFactory.getLogger(JobManager.class);

	public static int i = 0;
	public static final Gson sGson = new Gson();
	public static final ConcurrentMap<Long, Future> CURRRNT_JOBS = new ConcurrentHashMap<Long, Future>();
	public static final String SOLVER_CONFIG = "solver/shiftCompositionSolverConfig.xml";

	public JobManager() {
		
		taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.initialize();
		taskExecutor.setMaxPoolSize(100);
	
	}
	public long submitJob(final ShiftComposition shiftComposition) {
		if (shiftComposition == null)
			throw new NullPointerException();
		
		try {
			//Create job instance to be saved in the db
			Job givenJob = new Job();
			givenJob.setName(shiftComposition.getName());
			givenJob.setTaskDate(shiftComposition.getDate());
			givenJob.setStatus(Job.Status.CREATED);
			givenJob.setInput(sGson.toJson(shiftComposition));

			//Save job instance
			givenJob = repository.save(givenJob);
			
			//Prepare the job for submission 
			runnableJob.setJobID(givenJob.getId());
			runnableJob.setShiftComposition(shiftComposition);
			
			//Submit the job to the task executor
			Future<?> future = taskExecutor.submit(runnableJob);
			
			//Add job to currently running tasks
			CURRRNT_JOBS.put(givenJob.getId(), future);
		
			return givenJob.getId();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw e;
		}
	}
}
