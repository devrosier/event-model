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
	static public EventListener EVENT_LISTENER_D = new SimpleEventListener();
	static public Event EVENT_1 = new SimpleEvent();
	static public Event EVENT_2 = new AnotherEvent();

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
	 * Dry run publish when there are no listener registrations.
	 */
	public void testNoRegistrations() {
		EventManager em = new EventManager();
		em.publish(EVENT_1);
	}

	/**
	 * Register 2 listener for different events. Check the correct listener will
	 * be called on publish of an event.
	 */
	public void testListenerForEvent() {
		EventManager em = new EventManager();

		// register listeners for 2 different events
		em.registerListener(EVENT_LISTENER_A, new Class<?>[] { EVENT_1.getClass() });
		em.registerListener(EVENT_LISTENER_B, new Class<?>[] { EVENT_2.getClass() });
		em.registerListener(EVENT_LISTENER_C, new Class<?>[] { EVENT_2.getClass() });

		// check the listeners for 1 event
		List<EventListener> listeners = em.getListenersForEvent(EVENT_1);
		assertTrue(listeners.size() == 1);
		assertTrue(listeners.contains(EVENT_LISTENER_A));

		// check the listeners for other event
		List<EventListener> listeners2 = em.getListenersForEvent(EVENT_2);
		assertTrue(listeners2.size() == 2);
		assertTrue(listeners2.contains(EVENT_LISTENER_B));
		assertTrue(listeners2.contains(EVENT_LISTENER_C));
	}

	/**
	 * Register some listeners including listeners for all events. Check the all
	 * events listeners will be called on publish of an event
	 */
	public void testListenerForClasslessEvent() {
		EventManager em = new EventManager();

		em.registerListener(EVENT_LISTENER_A, new Class<?>[] {});
		em.registerListener(EVENT_LISTENER_B, new Class<?>[] { EVENT_1.getClass() });
		em.registerListener(EVENT_LISTENER_C, new Class<?>[] { EVENT_2.getClass() });

		// check listeners for one all events listener
		List<EventListener> listeners = em.getListenersForEvent(EVENT_1);
		assertTrue(listeners.size() == 2);
		assertTrue(listeners.contains(EVENT_LISTENER_A));
		assertTrue(listeners.contains(EVENT_LISTENER_B));

		// check listeners for multiple all events listener
		em.registerListener(EVENT_LISTENER_D, new Class<?>[] {});
		List<EventListener> listeners2 = em.getListenersForEvent(EVENT_2);
		assertTrue(listeners2.size() == 3);
		assertTrue(listeners2.contains(EVENT_LISTENER_A));
		assertTrue(listeners2.contains(EVENT_LISTENER_C));
		assertTrue(listeners2.contains(EVENT_LISTENER_D));
	}

	/**
	 * Register assorted listeners and then check that they deregister
	 */
	public void testDeregisterSpecificEventListener() {
		EventManager em = new EventManager();

		em.registerListener(EVENT_LISTENER_A, new Class<?>[] {});
		em.registerListener(EVENT_LISTENER_B, new Class<?>[] { EVENT_1.getClass() });
		em.registerListener(EVENT_LISTENER_C, new Class<?>[] { EVENT_2.getClass() });

		// confirm listener is registered then deregistered
		assertTrue(em.getListenersForEvent(EVENT_1).contains(EVENT_LISTENER_B));
		em.deregisterListener(EVENT_LISTENER_B);
		assertFalse(em.getListenersForEvent(EVENT_1).contains(EVENT_LISTENER_B));
		assertTrue(em.getListenersForEvent(EVENT_1).contains(EVENT_LISTENER_A));

	}

	/**
	 * Register assorted listeners and then check that they deregister
	 */
	public void testDeregisterAllEventListener() {
		EventManager em = new EventManager();

		em.registerListener(EVENT_LISTENER_A, new Class<?>[] {});
		em.registerListener(EVENT_LISTENER_B, new Class<?>[] { EVENT_1.getClass() });
		em.registerListener(EVENT_LISTENER_C, new Class<?>[] { EVENT_2.getClass() });

		// confirm listener is registered then deregistered
		assertTrue(em.getListenersForEvent(EVENT_1).contains(EVENT_LISTENER_A));
		em.deregisterListener(EVENT_LISTENER_A);
		assertFalse(em.getListenersForEvent(EVENT_1).contains(EVENT_LISTENER_A));
		assertTrue(em.getListenersForEvent(EVENT_1).contains(EVENT_LISTENER_B));

	}

	/**
	 * Test the publish engine. Note we can't assert much just that the method
	 * was called and returned successfully.
	 */
	public void testPublish() {

		EventManager em = new EventManager();

		em.registerListener(new SimpleEventListener(), new Class[] { SimpleEvent.class });
		em.registerListener(new SimpleEventListener(), new Class[] {});

		em.publish(new SimpleEvent());
		em.publish(new AnotherEvent());

		assertTrue(true);
	}

}
