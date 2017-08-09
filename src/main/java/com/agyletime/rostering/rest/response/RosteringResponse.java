package com.agyletime.rostering.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class RosteringResponse extends BaseResponse {

	private Object tasks;
	private Object error;


	public Object getTasks() {
		return tasks;
	}

	public Object getError() {
		return error;
	}

	public void setTasks(Object tasks) {
		this.tasks = tasks;
	}

	public void setError(Object error) {
		this.error = error;
	}

}
