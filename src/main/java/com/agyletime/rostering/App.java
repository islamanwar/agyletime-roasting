package com.agyletime.rostering;

import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import com.agyletime.roasting.AppTest;
import com.agyletime.rostering.model.ShiftComposition;
import com.agyletime.rostering.model.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Hello world!
 *
 */
public class App {
	
	public static final String SOLVER_CONFIG = "solver/shiftCompositionSolverConfig.xml";
	
	public static void main(String[] args) {
		
		//Read input
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
				.create();

		Reader reader = new InputStreamReader(
				App.class.getClassLoader().getResourceAsStream("json\\2-employees-4-tasks.json"));

		ShiftComposition shiftComposition = gson.fromJson(reader, ShiftComposition.class);
		//Set date to be available in each task
		if(shiftComposition != null && shiftComposition.getDate() != null){
			if(shiftComposition.getTasks() != null && !shiftComposition.getTasks().isEmpty()){
				for(Task task : shiftComposition.getTasks())
					task.setDate(shiftComposition.getDate());
			}
		}
		SolverFactory<ShiftComposition> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
		
		Solver<ShiftComposition> solver = solverFactory.buildSolver();
		
		ShiftComposition solvedShiftComposition =solver.solve(shiftComposition);
		System.out.println(gson.toJson(solvedShiftComposition.getTasks()));
	}
}
