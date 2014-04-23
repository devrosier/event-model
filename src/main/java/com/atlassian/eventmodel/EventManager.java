package com.atlassian.eventmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.atlassian.eventmodel.event.Event;
import com.atlassian.eventmodel.eventListener.EventListener;

public class EventManager {

	/**
	 * map of events registered for each listener (K event, V listeners)
	 */
	private Map<Class<?>, Collection<EventListener>> listenersForEvent = new HashMap<Class<?>, Collection<EventListener>>();
	/**
	 * map of classes registered for each listener (K listener, V events)
	 */
	private Map<EventListener, Collection<Class<?>>> eventsForListener = new HashMap<EventListener, Collection<Class<?>>>();
	/**
	 * list of listeners which respond to all event classes
	 */
	private Collection<EventListener> listenersForAllEvents = new HashSet<EventListener>();

	/**
	 * Event manager allows to registration of listeners for events. An
	 * EventListener may register to listen for 0 or N events by registering the
	 * Event class. If 0 event classes are specified then it will listen for ALL
	 * events. If N event classes are specified then it will listen for just
	 * those events. Listeners may be deregistered when no longer
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

		// retrieve event classes for this listener, and remove listener for
		// each
		Collection<Class<?>> eventClasses = eventsForListener.get(eventListener);
		if (eventClasses != null) {
			for (Class<?> eventClass : eventClasses) {
				Collection<EventListener> registeredListeners = listenersForEvent.get(eventClass);
				if (registeredListeners != null) {
					registeredListeners.remove(eventListener);
				}
			}
		}

		eventsForListener.remove(eventListener);
		
		listenersForAllEvents.remove(eventListener);

	}

	/**
	 * Register a listener to listen for certain events or all events. If you
	 * call this more than once for the same EventListener instance, then the
	 * previous one will first be unregistered, and the new one will therefore
	 * completely supersede the previous one.
	 * 
	 * @param eventListener
	 * @param classes
	 *            array of event classes that will publish to this listener
	 */
	public void registerListener(EventListener eventListener, Class<?>[] eventClasses) {

		// remove previous registration of this listener to avoid conflict
		deregisterListener(eventListener);

		// populate the map of listeners for each class
		for (Class<?> eventClass : eventClasses) {
			Collection<EventListener> listeners = listenersForEvent.get(eventClass);
			if (listeners == null)
				listeners = new HashSet<EventListener>();
			listeners.add(eventListener);
			listenersForEvent.put(eventClass, listeners);
		}

		// populate set of listeners to all classes
		if (eventClasses.length == 0) {
			listenersForAllEvents.add(eventListener);
		}

		// populate the map of classes for each listener (classes may be empty)
		eventsForListener.put(eventListener, Arrays.asList(eventClasses));

	}

	/**
	 * Publish will fire EventListener.eventHandler on each registered listener
	 * for this event
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
	 * 
	 * @return A List of EventListeners
	 */
	public List<EventListener> getListenersForEvent(Event event) {

		// the event class we are looking for
		Class<?> eventClass = event.getClass();

		// combined listener list (a) class specific and (b) broadcast
		List<EventListener> listeners = new ArrayList<EventListener>();

		// the list of class specific listeners
		Collection<EventListener> classSpecificListeners = listenersForEvent.get(eventClass);
		if (classSpecificListeners != null) {
			listeners.addAll(classSpecificListeners);
		}

		// and the listeners with no class
		listeners.addAll(listenersForAllEvents);

		return listeners;
	}

}
