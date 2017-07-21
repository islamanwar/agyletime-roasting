package com.agyletime.roasting;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import org.junit.Test;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import com.agyletime.rostering.App;
import com.agyletime.rostering.model.ShiftComposition;
import com.agyletime.rostering.model.Task;

/**
 * Unit test for simple App.
 */
public class BasicTest extends BaseTest {

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public BasicTest(String testName) {
		super(testName);
	}

	/**
	 * Expected Result Task #1 > Employee #1 
	 */
	@Test
	public void testEmployeeMatchesTask() {
		// Read input
		Reader reader = new InputStreamReader(
				App.class.getClassLoader().getResourceAsStream("json\\1-task-matches-1-employee.json"));

		ShiftComposition shiftComposition = gson.fromJson(reader, ShiftComposition.class);
		// Set date to be available in each task
		if (shiftComposition != null && shiftComposition.getDate() != null) {
			if (shiftComposition.getTasks() != null && !shiftComposition.getTasks().isEmpty()) {
				for (Task task : shiftComposition.getTasks())
					task.setDate(shiftComposition.getDate());
			}
		}
		SolverFactory<ShiftComposition> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);

		Solver<ShiftComposition> solver = solverFactory.buildSolver();

		ShiftComposition solvedShiftComposition = solver.solve(shiftComposition);
		List<Task> tasks = solvedShiftComposition.getTasks();
		// Task
		assertNotNull("Task #" + tasks.get(0).getId() + " should be assigned an employee", tasks.get(0).getEmployee());

	}
	/**
	 * Expected Result Task #1 > Not Assigned - Due to task location difference
	 */
	@Test
	public void testEmployeeDoesNotMatcheTaskLocation() {
		// Read input
		Reader reader = new InputStreamReader(
				App.class.getClassLoader().getResourceAsStream("json\\1-task-not-matches-1-employee_diff_location.json"));

		ShiftComposition shiftComposition = gson.fromJson(reader, ShiftComposition.class);
		// Set date to be available in each task
		if (shiftComposition != null && shiftComposition.getDate() != null) {
			if (shiftComposition.getTasks() != null && !shiftComposition.getTasks().isEmpty()) {
				for (Task task : shiftComposition.getTasks())
					task.setDate(shiftComposition.getDate());
			}
		}
		SolverFactory<ShiftComposition> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);

		Solver<ShiftComposition> solver = solverFactory.buildSolver();

		ShiftComposition solvedShiftComposition = solver.solve(shiftComposition);
		List<Task> tasks = solvedShiftComposition.getTasks();
		// Task
		assertNull("Task #" + tasks.get(0).getId() + " should NOT be assigned an employee", tasks.get(0).getEmployee());

	}
	
	/**
	 * Expected Result Task #1 > Not Assigned - Due to task interval difference
	 */
	@Test
	public void testEmployeeDoesNotMatcheTaskInterval() {
		// Read input
		Reader reader = new InputStreamReader(
				App.class.getClassLoader().getResourceAsStream("json\\1-task-not-matches-1-employee_diff_interval.json"));

		ShiftComposition shiftComposition = gson.fromJson(reader, ShiftComposition.class);
		// Set date to be available in each task
		if (shiftComposition != null && shiftComposition.getDate() != null) {
			if (shiftComposition.getTasks() != null && !shiftComposition.getTasks().isEmpty()) {
				for (Task task : shiftComposition.getTasks())
					task.setDate(shiftComposition.getDate());
			}
		}
		SolverFactory<ShiftComposition> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);

		Solver<ShiftComposition> solver = solverFactory.buildSolver();

		ShiftComposition solvedShiftComposition = solver.solve(shiftComposition);
		List<Task> tasks = solvedShiftComposition.getTasks();
		// Task
		assertNull("Task #" + tasks.get(0).getId() + " should NOT be assigned an employee", tasks.get(0).getEmployee());

	}
	/**
	 * Expected Result Task #1 > Not Assigned - Due to task skill difference
	 */
	@Test
	public void testEmployeeDoesNotMatcheTaskSkill() {
		// Read input
		Reader reader = new InputStreamReader(
				App.class.getClassLoader().getResourceAsStream("json\\1-task-not-matches-1-employee_diff_skill.json"));

		ShiftComposition shiftComposition = gson.fromJson(reader, ShiftComposition.class);
		// Set date to be available in each task
		if (shiftComposition != null && shiftComposition.getDate() != null) {
			if (shiftComposition.getTasks() != null && !shiftComposition.getTasks().isEmpty()) {
				for (Task task : shiftComposition.getTasks())
					task.setDate(shiftComposition.getDate());
			}
		}
		SolverFactory<ShiftComposition> solverFactory = SolverFactory.createFromXmlResource(SOLVER_CONFIG);

		Solver<ShiftComposition> solver = solverFactory.buildSolver();

		ShiftComposition solvedShiftComposition = solver.solve(shiftComposition);
		List<Task> tasks = solvedShiftComposition.getTasks();
		// Task
		assertNull("Task #" + tasks.get(0).getId() + " should NOT be assigned an employee", tasks.get(0).getEmployee());

	}
	
}
