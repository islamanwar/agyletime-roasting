package com.agyletime.rostering.rest.controller;

import java.util.List;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agyletime.rostering.model.ShiftComposition;
import com.agyletime.rostering.model.Task;
import com.agyletime.rostering.rest.response.RosteringResponse;

@RestController
public class SchedulingController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(SchedulingController.class);
	
	public static final String SOLVER_CONFIG = "solver/shiftCompositionSolverConfig.xml";
	
	@RequestMapping(method = RequestMethod.POST, path = "schedule", consumes={"application/json"}, produces = { "application/json" })
	public RosteringResponse schedule(@RequestBody ShiftComposition shiftComposition) {
		
		
		RosteringResponse serviceResponse = new RosteringResponse();

		SolverFactory<ShiftComposition> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);

		Solver<ShiftComposition> solver = solverFactory.buildSolver();

		ShiftComposition solvedShiftComposition = solver.solve(shiftComposition);

		List<Task> tasks = solvedShiftComposition.getTasks();
		
		serviceResponse.setTasks(tasks);
		//Set task's date to the date sent with the request
		if(tasks != null && !tasks.isEmpty()){
			for(Task task : tasks){
				task.setDate(shiftComposition.getDate());
			}
		}
		return serviceResponse;
	}
}
