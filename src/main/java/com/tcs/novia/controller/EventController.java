package com.tcs.novia.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.tcs.novia.model.Employee;
import com.tcs.novia.model.Event;
import com.tcs.novia.model.VoteCount;
import com.tcs.novia.model.enums.EventState;
import com.tcs.novia.repository.EventRepository;
import com.tcs.novia.service.EmployeeService;
import com.tcs.novia.service.EventResponseService;
import com.tcs.novia.service.EventService;
import com.tcs.novia.util.ImageProcessor;

@Controller
public class EventController {
	@Autowired
	private EventService eventService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EventResponseService eventResponseService;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	protected ImageProcessor imageProcessor;

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = "/createEvent", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public Event createEvent(
			@RequestParam(value = "eventName", required = true) final String eventName,
			@RequestParam(value = "eventDate", required = true) final String eventDate,
			@RequestParam(value = "details", required = true) final String details,
			@RequestParam(value = "startTime", required = false) final String startTime,
			@RequestParam(value = "finishTime", required = false) final String finishTime,
			@RequestParam(value = "duration", required = false) final String duration,
			@RequestParam(value = "photoFile", required = false) final MultipartFile photoFile) {
		
		String photo = null;
		if (null != photoFile && !photoFile.isEmpty()) {
			try {
				final byte[] bytes = photoFile.getBytes();
				photo = imageProcessor.resizeImage(bytes);
			} catch (final Exception e) {
				LOGGER.error("Error uploading photo::{}",e);
			}
		}
		
		return eventRepository.save(new Event(eventName, eventDate, details, startTime, finishTime, photo,duration, EventState.SCHEDULED));
	}
	
	@RequestMapping(value = "/addEventsFromJSON", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public List<Event> addEventsFromJSON(@RequestBody final List<Event> events){
		return eventRepository.saveAll(events);
	}

	@RequestMapping(value = "/deleteEvent/{eventID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void getAllEvents(@PathVariable("eventID") final int eventID) {
		eventRepository.deleteById(eventID);
	}
	@RequestMapping(value = "/getAllEvents", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Event> getAllEvents() {
		return eventService.getAllEvents();
	}

	@RequestMapping(value = "/getEventsByDate", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<Event> getEventsByDate(@RequestParam(value = "eventDate", required = true) final String eventDate) {
		return eventService.getEventsByDate(eventDate);
	}

	@RequestMapping(value = "/saveEventResponseWithComment/{employeeID}/{eventID}/{vote}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void saveEventResponseWithComment(@PathVariable("employeeID") final long employeeID,
			@PathVariable("eventID") final int eventID, @PathVariable("vote") final String vote,
			@RequestParam(value = "comment", required = false) final String comment) {

		final Employee emp=employeeService.findByEmployeeID(employeeID);
		final Event event = eventService.getEvent(eventID);
		if (null != event && null!=emp) {
			eventResponseService.saveWithComment(employeeID, eventID, vote, comment);
		}
	}

	@RequestMapping(value = "/getVoteCountForAnEvent/{eventID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public VoteCount getVoteCountForAnEvent(@PathVariable("eventID") final int eventID) {
		return eventResponseService.getVoteCountForAnEvent(eventID, "" + eventID);
	}

	@RequestMapping(value = "/getVoteCountForAllEvents", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<VoteCount> getVoteCountForAllEvents() {
		List<VoteCount> voteCountForAllEvents = null;
		final List<Event> allEvents = eventService.getAllEvents();
		if (null != allEvents && !allEvents.isEmpty()) {
			voteCountForAllEvents = new ArrayList<>(allEvents.size());
			for (final Event eachEvent : allEvents) {
				final VoteCount voteCountOfAnEvent = eventResponseService.getVoteCountForAnEvent(eachEvent.getEventID(),
						eachEvent.getEventName());
				voteCountForAllEvents.add(voteCountOfAnEvent);
			}
		}

		return voteCountForAllEvents;
	}
}