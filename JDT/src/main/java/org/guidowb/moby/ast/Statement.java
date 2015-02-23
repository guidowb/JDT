package org.guidowb.moby.ast;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

public class Statement {

	private String statement;
	private Map<String, Object> arguments = new HashMap<String, Object>();
	
	public Statement(String statement) { this.statement = statement; }
	public @JsonAnySetter void put(String argument, Object value) { arguments.put(argument, value); }
	public String getStatement() { return statement; }
	public @JsonAnyGetter Map<String, Object> getArguments() { return arguments; }
}
