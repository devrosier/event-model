package com.atlassian.eventmodel.event;

import java.util.Map;

public interface Event {

	public String getName();

	public void setName(String name);

	public Map<String, Object> getDataMap();

	public void setDataMap(Map<String, Object> dataMap);

}
