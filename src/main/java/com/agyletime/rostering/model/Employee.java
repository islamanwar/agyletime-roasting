package com.agyletime.rostering.model;

import java.util.Arrays;
import java.util.List;

public class Employee {

	private long id;
	private String name;
	private List<String> locations;
	private Interval[] intervals;
	private List<Skill> skills;

	public boolean worksDuring(Task task) {
		
		boolean worksDuring = false;
		for (Interval interval : intervals) {
			if(interval.contains(task.getInterval())){
				worksDuring = true;
				break;
			}
		}
		return worksDuring;
	}

	public boolean hasLocation(String location) {
		boolean hasLocation = false;
		if (locations != null && !locations.isEmpty()) {
			hasLocation = locations.contains(location);
		}
		return hasLocation;
	}
	
	public boolean hasSkill(Skill skill) {
		boolean hasSkill = false;
		if (skills != null && !skills.isEmpty()) {
			hasSkill = skills.contains(skill);
		}
		return hasSkill;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<String> getLocations() {
		return locations;
	}

	public Interval[] getIntervals() {
		return intervals;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}

	public void setIntervals(Interval[] intervals) {
		this.intervals = intervals;
	}

	public void setSkills(List<Skill> skill) {
		this.skills = skill;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", locations=" + locations + ", intervals="
				+ Arrays.toString(intervals) + ", skills=" + skills + "]";
	}

	

}
