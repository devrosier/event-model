package com.atlassian.eventmodel.eventListener;

import com.atlassian.eventmodel.event.Event;

public interface EventListener {
	
	
	public void handleEvent(Event event);
	
	public Class<Event>[] getClasses();
	
	public String getName();
	
	public void setName(String name);
}
