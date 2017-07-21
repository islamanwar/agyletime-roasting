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
public class AppTest extends BaseTest {

	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * Expected Result Task #1 > Employee #2 Task #2 > Not assigned - No
	 * employee works during the whole task interval Task #3 > Not assigned - No
	 * employee works at the task's location Task #4 > Employee #1
	 */
	@Test
	public void test2Employee4Tasks() {
		// Read input
		Reader reader = new InputStreamReader(
				App.class.getClassLoader().getResourceAsStream("json\\2-employees-4-tasks.json"));

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
		assertNull("Task #" + tasks.get(1).getId() + " should NOT be assigned an employee", tasks.get(1).getEmployee());
		assertNull("Task #" + tasks.get(2).getId() + " should NOT be assigned an employee", tasks.get(2).getEmployee());
		assertNotNull("Task #" + tasks.get(3).getId() + " should be assigned an employee", tasks.get(3).getEmployee());

		assertEquals("Task #1 should be assigned Employee #2", 2, tasks.get(0).getEmployee().getId());
		assertEquals("Task #4 should be assigned Employee #1", 1, tasks.get(3).getEmployee().getId());

	}
}
