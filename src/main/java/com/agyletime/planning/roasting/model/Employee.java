package com.agyletime.planning.roasting.model;

import java.util.Arrays;
import java.util.List;

public class Employee {

	private long id;
	private String name;
	private List<String> locations;
	private Interval[] intervals;
	private Skill[] skills;

	public boolean worksDuring(Interval interval2) {
		
		boolean worksDuring = false;
		for (Interval interval : intervals) {
			if(interval.contains(interval2)){
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

	public Skill[] getSkills() {
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

	public void setSkills(Skill[] skill) {
		this.skills = skill;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", locations=" + locations + ", intervals="
				+ Arrays.toString(intervals) + ", skills=" + Arrays.toString(skills) + "]";
	}

}
