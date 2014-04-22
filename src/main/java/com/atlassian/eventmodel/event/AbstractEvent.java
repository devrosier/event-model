package com.atlassian.eventmodel.event;

import java.util.HashMap;
import java.util.Map;

abstract public class AbstractEvent implements Event {

	/**
	 * Name of event
	 */
	private String name;

	/**
	 * Description of event
	 */
	private String description;

	/**
	 * Map of data to pass to listener
	 */
	private Map<String, Object> dataMap = new HashMap<String, Object>();

	public AbstractEvent() {
		// TODO Auto-generated constructor stub
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

}
