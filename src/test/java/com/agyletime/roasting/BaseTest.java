package com.agyletime.roasting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import junit.framework.TestCase;

public abstract class BaseTest extends TestCase{

	protected static final Logger LOGGER = LoggerFactory.getLogger(AppTest.class);

	protected static final String SOLVER_CONFIG = "solver/shiftCompositionSolverConfig.xml";

	protected static Gson gson = new GsonBuilder().setPrettyPrinting().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
	
	public BaseTest(String testName) {
		super(testName);
	}
}
