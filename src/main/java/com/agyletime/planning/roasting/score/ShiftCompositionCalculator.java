package com.agyletime.planning.roasting.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import com.agyletime.planning.roasting.model.Employee;
import com.agyletime.planning.roasting.model.Interval;
import com.agyletime.planning.roasting.model.ShiftComposition;
import com.agyletime.planning.roasting.model.Task;

public class ShiftCompositionCalculator implements EasyScoreCalculator<ShiftComposition> {
	private static long COUNT = 0;
	public HardMediumSoftScore calculateScore(ShiftComposition solution) {

		int hardScore = 0;
		int mediumScore = 0;
		int softScore = 0;
		Map<Employee, List<Interval>> employeeOccupiedIntervals = new HashMap<Employee, List<Interval>>(); 
		for (Employee employee : solution.getEmployees()) {
			boolean taskHasLocation = false, employeeMatchTaskLocation = false;
			for (Task task : solution.getTasks()) {
				taskHasLocation = !StringUtils.isEmpty(task.getLocation());
				if (task.getEmployee() != null && task.getEmployee().equals(employee)) {
					//If task has a location, the employee must be available to work in that location
					if (taskHasLocation) {
						employeeMatchTaskLocation = employee.hasLocation(task.getLocation());
					}
					
					if (taskHasLocation && !employeeMatchTaskLocation)
						hardScore--;
					//The employee must be free at the time of the task
					if(!employeeOccupiedIntervals.containsKey(employee)){
						employeeOccupiedIntervals.put(employee, new ArrayList<Interval>());
					}
					if(task.getInterval() != null){
						if(employee.getIntervals() != null && employee.getIntervals().length > 0){
							boolean isEmployeeFreeToWork = false;
							for(Interval interval : employee.getIntervals()){
								if(interval.contains(task.getInterval()) && !isIntervalOccupiedForEmployee(employeeOccupiedIntervals.get(employee), task.getInterval())){
									isEmployeeFreeToWork = true;
									employeeOccupiedIntervals.get(employee).add(task.getInterval());
								}
							}
							if(!isEmployeeFreeToWork)
								hardScore--;
						}else{
							hardScore--;
						}
					}
					
				}else{
					mediumScore--;
				}
			}
		}

		return HardMediumSoftScore.valueOf(hardScore, mediumScore, softScore);
	}
	private boolean isIntervalOccupiedForEmployee(List<Interval> occupiedIntervals, Interval interval) {
		boolean intervalOccupied = false;
		
		if(occupiedIntervals != null && occupiedIntervals.size() > 0){
			for(Interval occupiedInterval : occupiedIntervals){
				if(occupiedInterval.intersects(interval) || interval.intersects(occupiedInterval)){
					intervalOccupied = true;
					break;
				}
			}
		}
		return intervalOccupied;
	}
}
