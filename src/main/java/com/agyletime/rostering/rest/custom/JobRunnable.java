package com.agyletime.rostering.rest.custom;

import java.util.Date;

import org.drools.core.util.StringUtils;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.agyletime.rostering.model.ShiftComposition;
import com.agyletime.rostering.rest.controller.SchedulingController;
import com.agyletime.rostering.rest.model.Job;
import com.agyletime.rostering.rest.repository.JobRepository;
import com.agyletime.rostering.util.Utils;
import com.google.gson.Gson;

@Component
@Scope("prototype")
public class JobRunnable implements Runnable {

	@Autowired
	private JobRepository repository;

	public static final Gson sGson = new Gson();
	public static final String SOLVER_CONFIG = "solver/shiftCompositionSolverConfig.xml";
	private long jobID;
	private ShiftComposition shiftComposition;

	protected static final Logger LOGGER = LoggerFactory.getLogger(SchedulingController.class);

	@Override
	public void run() {

		Job runningJob = repository.findOne(jobID);
		if (runningJob == null) {
			// Job not found in the database
			LOGGER.error("Job ID #" + jobID + " Not found in db.");
			return;
		}
		try {
			// Prepare initial job state
			runningJob.setStartDate(new Date());
			runningJob.setStatus(Job.Status.RUNNING);

			// Save task state before execution
			runningJob = repository.save(runningJob);

			// Start actual execution
			ShiftComposition solvedShiftComposition = new ShiftComposition();
			SolverFactory<ShiftComposition> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
			Solver<ShiftComposition> solver = solverFactory.buildSolver();
			solvedShiftComposition = solver.solve(shiftComposition);

			// Prepare final job state
			runningJob.setStatus(Job.Status.SUCCEEDED);
			runningJob.setOutput(sGson.toJson(solvedShiftComposition));
			runningJob.setEndDate(new Date());

			// Save task state after execution
			repository.save(runningJob);

			JobManager.CURRRNT_JOBS.remove(jobID);

			// Call callback uri
			if (!StringUtils.isEmpty(runningJob.getCallbackUri())) {
				try {
					Utils.sendPost(runningJob.getCallbackUri() + "?jobId=" + jobID + "&status=SUCCESS");
				} catch (Exception ex) {
					LOGGER.error("Error while calling callback uri", ex);
				}
			}

		} catch (Exception e) {
			if (e instanceof InterruptedException) {
				// Thread was cancelled probably by Cancel API call. do nothing
			} else {
				LOGGER.error(e.getMessage(), e);
				// Update job with the failed status
				runningJob.setStatus(Job.Status.FAILED);
				runningJob.setExtraDetails(e.getMessage());
				runningJob.setEndDate(new Date());
				repository.save(runningJob);
				
				// Call callback uri
				if (!StringUtils.isEmpty(runningJob.getCallbackUri())) {
					
					try {
						Utils.sendPost(runningJob.getCallbackUri() + "?jobId=" + jobID + "&status=FAILED");
					} catch (Exception ex) {
						LOGGER.error("Error while calling callback uri", ex);
					}
				}
			}

		}
	}

	public long getJobID() {
		return jobID;
	}

	public void setJobID(long jobID) {
		this.jobID = jobID;
	}

	public ShiftComposition getShiftComposition() {
		return shiftComposition;
	}

	public void setShiftComposition(ShiftComposition shiftComposition) {
		this.shiftComposition = shiftComposition;
	}

}
