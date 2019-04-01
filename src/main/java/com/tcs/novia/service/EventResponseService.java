package com.tcs.novia.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.novia.model.Employee;
import com.tcs.novia.model.Event;
import com.tcs.novia.model.EventResponse;
import com.tcs.novia.model.EventResponseEnvelope;
import com.tcs.novia.model.VoteCount;
import com.tcs.novia.model.enums.EventState;
import com.tcs.novia.model.enums.Vote;
import com.tcs.novia.repository.EmployeeRepository;
import com.tcs.novia.repository.EventRepository;
import com.tcs.novia.repository.EventResponseRepository;

@Service
public class EventResponseService {

	@Autowired
	EventResponseRepository eventResponseRepository;

	@Autowired
	EventRepository eventRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	public EventResponse save(final long employeeID, final int eventID, final String vote) {
		return eventResponseRepository
				.save(new EventResponse(eventID, employeeID, Vote.valueOf(vote), new Timestamp(new Date().getTime())));
	}

	public EventResponse saveWithComment(final long employeeID, final int eventID, final String vote,
			final String comment) {
		return eventResponseRepository.save(new EventResponse(eventID, employeeID, Vote.valueOf(vote), comment,
				new Timestamp(new Date().getTime())));
	}

	public VoteCount getVoteCountForAnEvent(final int eventID, final String eventName) {
		Integer likeCount = eventResponseRepository.getVoteCountForAnEvent(eventID, Vote.LIKE);
		Integer dislikeCount = eventResponseRepository.getVoteCountForAnEvent(eventID, Vote.DISLIKE);

		likeCount = null != likeCount ? likeCount : 0;
		dislikeCount = null != dislikeCount ? dislikeCount : 0;

		return new VoteCount(eventName, likeCount, dislikeCount);
	}

	public List<EventResponseEnvelope> getLatestResponses(final int eventID) {
		final List<Event> runningEvents = eventRepository.findByState(EventState.RUNNING);

		if (null != runningEvents && !runningEvents.isEmpty()) {

			for (final Event event : runningEvents) {
				if (eventID == event.getEventID()) {
					final List<EventResponse> eventResponses = eventResponseRepository
							.findTop7ByEventIDOrderByTimeStampDesc(eventID);
					final List<EventResponseEnvelope> eventResponseEnvelope = new ArrayList<>();
					for (final EventResponse eventResponse : eventResponses) {
						final List<Employee> employeeList = employeeRepository
								.findByEmployeeID(eventResponse.getEmployeeID());
						if (null != employeeList && !employeeList.isEmpty()) {
							eventResponseEnvelope.add(new EventResponseEnvelope(eventResponse, employeeList.get(0)));
						} else {
							LOGGER.error("No employee found against:: {}", eventResponse.getEmployeeID());
						}
					}
					return eventResponseEnvelope;
				}
			}

		}
		return null;
	}

}
