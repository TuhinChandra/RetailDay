package com.tcs.KingfisherDay.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcs.KingfisherDay.model.Employee;
import com.tcs.KingfisherDay.model.Event;
import com.tcs.KingfisherDay.model.EventResponse;
import com.tcs.KingfisherDay.model.EventResponseEnvelope;
import com.tcs.KingfisherDay.model.VoteCount;
import com.tcs.KingfisherDay.model.enums.EventState;
import com.tcs.KingfisherDay.model.enums.Vote;
import com.tcs.KingfisherDay.repository.EmployeeRepository;
import com.tcs.KingfisherDay.repository.EventRepository;
import com.tcs.KingfisherDay.repository.EventResponseRepository;

@Service
public class EventResponseService {

	@Autowired
	EventResponseRepository eventResponseRepository;

	@Autowired
	EventRepository eventRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	public EventResponse save(String emailID, int eventID, String vote) {
		return eventResponseRepository.save(
				new EventResponse(eventID, emailID, Vote.valueOf(vote), new Timestamp(new Date().getTime())));
	}

	public EventResponse saveWithComment(String emailID, int eventID, String vote, String comment) {
		return eventResponseRepository.save(new EventResponse(eventID, emailID, Vote.valueOf(vote), comment,
				new Timestamp(new Date().getTime())));
	}

	public VoteCount getVoteCountForAnEvent(int eventID, String eventName) {
		Integer likeCount= eventResponseRepository.getVoteCountForAnEvent(eventID, Vote.LIKE);
		Integer dislikeCount= eventResponseRepository.getVoteCountForAnEvent(eventID, Vote.DISLIKE);
		
		likeCount=null!=likeCount?likeCount:0;
		dislikeCount=null!=dislikeCount?dislikeCount:0;
		
		return new VoteCount(eventName, likeCount, dislikeCount);
	}

	public List<EventResponseEnvelope> getLatestResponses(int eventID) {
		List<Event> runningEvents = eventRepository.findByState(EventState.RUNNING);
		
		if (null!=runningEvents && !runningEvents.isEmpty()) {
			
			for (Event event : runningEvents) {
				if (eventID == event.getEventID()) {
					List<EventResponse> eventResponses = eventResponseRepository
							.findTop7ByEventIDOrderByTimeStampDesc(eventID);
					List<EventResponseEnvelope> eventResponseEnvelope = new ArrayList<EventResponseEnvelope>();
					for (EventResponse eventResponse : eventResponses) {
						List<Employee> employeeList=employeeRepository.findByEmailID(eventResponse.getEmployeeEmail());
						if(null!=employeeList && !employeeList.isEmpty()) {
							eventResponseEnvelope.add(new EventResponseEnvelope(eventResponse, employeeList.get(0)));
						}else {
							System.err.println("No employee found against:: "+eventResponse.getEmployeeEmail());
						}
					}
					return eventResponseEnvelope;
				}
			}
			
		}
		return null;
	}

}
