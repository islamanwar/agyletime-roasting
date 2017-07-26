package com.agyletime.rostering.model;

/**
 * A task conflict is a collision between 2 tasks occurring at intersecting intervals.
 * 2 conflicting tasks can't be assigned to the same Employee
 * Task conflict is calculated during initialization and not modified during score calculation.
 */
public class TaskConflict {

	private Task leftTask;
	private Task rightTask;
	private int conflictCount;

	
	public TaskConflict(Task leftTask, Task rightTask) {
		super();
		this.leftTask = leftTask;
		this.rightTask = rightTask;
	}

	public Task getLeftTask() {
		return leftTask;
	}

	public Task getRightTask() {
		return rightTask;
	}

	public void setLeftTask(Task leftTask) {
		this.leftTask = leftTask;
	}

	public void setRightTask(Task rightTask) {
		this.rightTask = rightTask;
	}

	public int getConflictCount() {
		return conflictCount;
	}

	public void setConflictCount(int conflictCount) {
		this.conflictCount = conflictCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftTask == null) ? 0 : leftTask.hashCode());
		result = prime * result + ((rightTask == null) ? 0 : rightTask.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskConflict other = (TaskConflict) obj;
		if (leftTask == null) {
			if (other.leftTask != null)
				return false;
		} else if (!leftTask.equals(other.leftTask))
			return false;
		if (rightTask == null) {
			if (other.rightTask != null)
				return false;
		} else if (!rightTask.equals(other.rightTask))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskConflict [leftTask=" + leftTask.getId() + ", rightTask=" + rightTask.getId() + "]";
	}
	
	
	
}
