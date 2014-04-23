package com.atlassian.eventmodel.eventListener;

import com.atlassian.eventmodel.event.Event;

public class SimpleEventListener implements EventListener {

	private String name;

	public void handleEvent(Event event) {
		System.out.println("Listener " + this.getClass().getName() + " called for event " + event.getClass().getName()
				+ " - " + event.getName());
	}

	public Class<Event>[] getClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
