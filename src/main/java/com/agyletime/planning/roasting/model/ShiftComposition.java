package com.agyletime.planning.roasting.model;

import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

@PlanningSolution
public class ShiftComposition {

	private List<Employee> employees;

	private List<Task> tasks;

	private HardMediumSoftScore score;

	public ShiftComposition() {
	}

	ShiftComposition(List<Employee> employees, List<Task> tasks) {
		this.employees = employees;
		this.tasks = tasks;
	}

	@ProblemFactCollectionProperty
	@ValueRangeProvider(id = "employeeRange")
	public List<Employee> getEmployees() {
		return employees;
	}

	@PlanningEntityCollectionProperty
	public List<Task> getTasks() {
		return tasks;
	}

	@PlanningScore
	public HardMediumSoftScore getScore() {
		return score;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void setScore(HardMediumSoftScore score) {
		this.score = score;
	}

	@ProblemFactCollectionProperty
	public List<TaskConflict> calculateTaskConflict() {
		List<TaskConflict> taskConflicts = new ArrayList<TaskConflict>();
		for (Task leftTask : tasks) {
			for (Task rightTask : tasks) {
				if (leftTask.getId() < rightTask.getId()) {
					int conflictCount = 0;
					// Check if 2 tasks have conflicting intervals
					if (leftTask.getInterval() != null && rightTask.getInterval() != null
							&& (leftTask.getInterval().intersects(rightTask.getInterval())
									|| rightTask.getInterval().intersects(leftTask.getInterval())))
						conflictCount++;

					// TODO check other conflicts
					if (conflictCount > 0)
						taskConflicts.add(new TaskConflict(leftTask, rightTask));
				}

			}

		}
		return taskConflicts;
	}

	@Override
	public String toString() {
		return "ShiftComposition [employees=" + employees + ", tasks=" + tasks + ", score=" + score + "]";
	}

}
