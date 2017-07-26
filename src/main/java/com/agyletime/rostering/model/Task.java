package com.agyletime.rostering.model;

import java.util.Date;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.agyletime.rostering.rest.custom.*;

@PlanningEntity
public class Task {

	private int id;
	@JsonSerialize(using=JsonDateSerializer.class)
	private Date date;
	private Skill requiredSkill;
	private String location;
	private Interval interval;

	private Employee employee;

	public int getId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	public Skill getRequiredSkill() {
		return requiredSkill;
	}

	public String getLocation() {
		return location;
	}

	public Interval getInterval() {
		return interval;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setRequiredSkill(Skill requiredSkill) {
		this.requiredSkill = requiredSkill;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setInterval(Interval interval) {
		this.interval = interval;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@PlanningVariable(nullable = true, valueRangeProviderRefs = { "employeeRange" })
	public Employee getEmployee() {
		return employee;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", date=" + date + ", requiredSkill=" + requiredSkill + ", location=" + location
				+ ", interval=" + interval + ", employee=" + employee + "]";
	}

	

}
