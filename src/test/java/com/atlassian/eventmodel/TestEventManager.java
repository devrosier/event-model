package com.atlassian.eventmodel;

import java.util.List;

import com.atlassian.eventmodel.eventListener.*;
import com.atlassian.eventmodel.event.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class TestEventManager extends TestCase {

	static public EventListener EVENT_LISTENER_A = new SimpleEventListener();
	static public EventListener EVENT_LISTENER_B = new SimpleEventListener();
	static public EventListener EVENT_LISTENER_C = new SimpleEventListener();
	/**
	 * Create the test case
	 * 
	 * @param testName
	 */
	public TestEventManager(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TestEventManager.class);
	}

	/**
	 * Register single listener against single event class. Check the listener
	 * will be called upon publish of same event.
	 */
	public void testListenerForClassEvent() {
		EventManager em = new EventManager();

		// register a listener for an event class
		em.registerListener(EVENT_LISTENER_A, new Class<?>[] { SimpleEvent.class });
		// register a listener for an other class
		em.registerListener(EVENT_LISTENER_B, new Class<?>[] { AnotherEvent.class });

		// get the listeners
		List<EventListener> listeners = em.getListenersForEvent(new SimpleEvent());

		// check we got the correct one
		System.out.println("for class listener size=" + listeners.size());
		assertTrue(listeners.size() == 1 && listeners.get(0) == EVENT_LISTENER_A);
	}

	/**
	 * Register single listener against no class. Check the listener will be
	 * called upon publish of any event/
	 */
	public void testListenerForClasslessEvent() {
		EventManager em = new EventManager();

		// register a listener for an event class
		em.registerListener(EVENT_LISTENER_A, new Class<?>[] {});
		// register a listener for an other class
		em.registerListener(EVENT_LISTENER_B, new Class<?>[] { YetAnotherEvent.class });
		// register a listener for no class
		em.registerListener(EVENT_LISTENER_C, new Class<?>[] { AnotherEvent.class });

		// get the listeners
		List<EventListener> listeners = em.getListenersForEvent(new SimpleEvent());

		// check we go the correct one
		assertTrue(listeners.size() == 1 && listeners.get(0) == EVENT_LISTENER_A);
	}

	/**
	 * Register 1 listener with an event class, and 1 listener without an event
	 * class, and 1 listener with an irrelevant event class
	 */
	public void testListenerForAssortedEvents() {
		EventManager em = new EventManager();

		// register a listener for an event class
		em.registerListener(EVENT_LISTENER_A, new Class<?>[] { SimpleEvent.class});
		// register a listener for an other class
		em.registerListener(EVENT_LISTENER_B, new Class<?>[] { AnotherEvent.class });
		// register a listener for no class
		em.registerListener(EVENT_LISTENER_C, new Class<?>[] {});

		// get all listeners for event
		List<EventListener> listeners = em.getListenersForEvent(new SimpleEvent());

		// check we got the correct ones
		assertTrue(listeners.size() == 2 && listeners.contains(EVENT_LISTENER_A)
				&& listeners.contains(EVENT_LISTENER_C));
	}

	/**
	 * Register a listener a class, then check it deregisters
	 */
	public void testDeregisterClassEventListener() {
		EventManager em = new EventManager();

		// register a listener for an event class
		em.registerListener(EVENT_LISTENER_A, new Class<?>[] { SimpleEvent.class});
		// register a listener for an other class
		em.registerListener(EVENT_LISTENER_B, new Class<?>[] { AnotherEvent.class });
		// register a listener for no class
		em.registerListener(EVENT_LISTENER_C, new Class<?>[] {});

		SimpleEvent simpleEvent = new SimpleEvent();
		List<EventListener> listenersBeforeDereg = em.getListenersForEvent(simpleEvent);
		em.deregisterListener(EVENT_LISTENER_A);
		List<EventListener> listenersAfterDereg = em.getListenersForEvent(simpleEvent);

		// check we unregistered correctly
		assertTrue(listenersBeforeDereg.contains(EVENT_LISTENER_A)
				&& !listenersAfterDereg.contains(EVENT_LISTENER_A));
	}

	/**
	 * Register a listener a class, then check it deregisters
	 */
	public void testDeregisterClasslessEventListener() {
		EventManager em = new EventManager();

		// register a listener for an event class
		em.registerListener(EVENT_LISTENER_A, new Class<?>[] { SimpleEvent.class});
		// register a listener for an other class
		em.registerListener(EVENT_LISTENER_B, new Class<?>[] { AnotherEvent.class });
		// register a listener for no class
		em.registerListener(EVENT_LISTENER_C, new Class<?>[] {});

		SimpleEvent simpleEvent = new SimpleEvent();
		List<EventListener> listenersBeforeDereg = em.getListenersForEvent(simpleEvent);
		em.deregisterListener(EVENT_LISTENER_C);
		List<EventListener> listenersAfterDereg = em.getListenersForEvent(simpleEvent);

		// check we go the correct one
		assertTrue(listenersBeforeDereg.contains(EVENT_LISTENER_C)
				&& !listenersAfterDereg.contains(EVENT_LISTENER_C));
	}

	/**
	 * Test the publish engine. Note we can't assert much just that the method
	 * was called and returned successfully.
	 */
	public void testPublish() {

		EventManager em = new EventManager();

		// one class specific listener
		em.registerListener(new SimpleEventListener(), new Class[] { SimpleEvent.class });
		// one no class listener (responds to all events)
		em.registerListener(new SimpleEventListener(), new Class[] {});

		em.publish(new SimpleEvent());

		assertTrue(true);
	}

}
