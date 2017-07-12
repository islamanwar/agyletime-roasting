package com.agyletime.planning.roasting;

import java.io.InputStreamReader;
import java.io.Reader;

import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import com.agyletime.planning.roasting.model.ShiftComposition;
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
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Reader reader = new InputStreamReader(
				App.class.getClassLoader().getResourceAsStream("json\\2-employees-3-tasks.json"));

		ShiftComposition shiftComposition = gson.fromJson(reader, ShiftComposition.class);

		SolverFactory<ShiftComposition> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);
		
		Solver<ShiftComposition> solver = solverFactory.buildSolver();
		
		ShiftComposition solvedShiftComposition =solver.solve(shiftComposition);
		System.out.println(gson.toJson(solvedShiftComposition.getTasks()));
	}
}
