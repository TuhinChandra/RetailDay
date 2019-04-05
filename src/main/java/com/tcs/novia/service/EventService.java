package com.tcs.novia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.novia.model.Event;
import com.tcs.novia.model.enums.EventState;
import com.tcs.novia.repository.EventRepository;

@Service
public class EventService {

	@Autowired
	EventRepository eventRepository;

	public List<Event> getAllEvents() {
		return eventRepository.findAllByOrderByEventID();
	}
	public List<Event> getEventsByDate(String eventDate) {
		return eventRepository.findByEventDateOrderByEventID(eventDate);
	}

	public Event getEvent(int eventID) {
		return eventRepository.findByEventID(eventID);
	}
	/*
	 * public List<Event> getCurrentEvent() { return
	 * eventRepository.findByState(EventState.RUNNING); }
	 */

	public void changeEventState(int eventID, String state) {
		Event activeEvent = eventRepository.findByEventID(eventID);
		activeEvent.setState(EventState.valueOf(state));
		eventRepository.save(activeEvent);
	}

	public void closeAllEvents() {
		List<Event> events = eventRepository.findByState(EventState.RUNNING);
		if(null!=events && !events.isEmpty()) {
			for (Event event : events) {
				event.setState(EventState.COMPLETED);
				eventRepository.save(event);
			}
		}
	}

}