package com.atlassian.eventmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.atlassian.eventmodel.event.Event;
import com.atlassian.eventmodel.eventListener.EventListener;

public class EventManager {

	/**
	 * list of all listeners registered
	 */
	private Set<EventListener> allListeners = new HashSet<EventListener>();
	/**
	 * list of those listeners registered without an event class
	 */
	private Set<EventListener> listenersForNoClass = new HashSet<EventListener>();
	/**
	 * map of listeners registered for each class
	 */
	private Map<Class<?>, List<EventListener>> listenersForClass = new HashMap<Class<?>, List<EventListener>>();

	/**
	 * Event manager allows to registration of listeners for events. An
	 * EventListener may register to listen for 0 or N events by registering the
	 * Event class. If 0 event classes are specified then it will listen for
	 * ALL events. If N event classes are specified then it will listen for
	 * just those events. Listeners may be deregistered when no longer
	 * required/active.
	 */
	public EventManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Deregister an EventListener, so it will no longer listen for Events. Will
	 * be ignored if the EventListener it was never registered in the first
	 * place.
	 * 
	 * @param eventListener
	 */
	public void deregisterListener(EventListener eventListener) {

		// remove from classless listeners
		listenersForNoClass.remove(eventListener);

		// iterate listeners for each event class and remove this listener
		for (List<EventListener> classEventListeners : listenersForClass
				.values()) {
			classEventListeners.remove(eventListener);
		}

		// remove from all listeners
		allListeners.remove(eventListener);

	}

	/**
	 * Register a listener to listen for certain events or all events. If you
	 * call this more than once for the same EventListener instance, then the
	 * previous one will first be unregistered, and the new one will therefore completely supercede the previous one.
	 * 
	 * @param eventListener
	 * @param classes
	 *            array of event classes that will publish to this listener
	 */
	public void registerListener(EventListener eventListener,
			Class<?>[] eventClasses) {

		// remove previous registration of this listener to avoid conflict
		deregisterListener(eventListener);
		
		// add the listener to master list
		if (!allListeners.contains(eventListener)) {
			allListeners.add(eventListener);
		}

		// if class array param is empty then add to NoClass list
		if (eventClasses.length == 0) {
			listenersForNoClass.add(eventListener);
		}

		// if class array param have entries then lodge listener against each event class
		for (Class<?> eventClass : eventClasses) {

			List<EventListener> listenerList = listenersForClass
					.get(eventClass);
			if (listenerList == null) {
				listenerList = new ArrayList<EventListener>();
			}
			listenerList.add(eventListener);
			listenersForClass.put(eventClass, listenerList);
		}
	}

	/**
	 * Publish will fire EventListener.eventHandler on each registered listener for this event
	 * 
	 * @param event
	 *            the event to send to registered EventListeners
	 */
	public void publish(Event event) {
		for (EventListener eventListener : getListenersForEvent(event)) {
			eventListener.handleEvent(event);
		}
	}

	/**
	 * Return all EventListeners registered for a specific Event
	 * @return A List of EventListeners
	 */
	public List<EventListener> getListenersForEvent(Event event) {

		// the event class we are looking for
		Class<?> eventClass = (Class<?>) event.getClass();

		// combined listener list (a) class specific and (b) broadcast
		List<EventListener> combinedListenerList = new ArrayList<EventListener>();

		// the list of class specific listeners
		List<EventListener> classSpecificListeners = listenersForClass
				.get(eventClass);
		if (classSpecificListeners != null) {
			combinedListenerList.addAll(classSpecificListeners);
		}

		// and the listeners with no class
		combinedListenerList.addAll(listenersForNoClass);

		return combinedListenerList;
	}

}
