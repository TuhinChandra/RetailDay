package com.tcs.novia.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tcs.novia.model.Event;
import com.tcs.novia.model.VoteCount;
import com.tcs.novia.service.EventResponseService;
import com.tcs.novia.service.EventService;

@Controller
public class EventController {
	@Autowired
	EventService eventService;

	@Autowired
	EventResponseService eventResponseService;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@RequestMapping(value = "/getAllEvents", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Event> getAllEvents() {
		return eventService.getAllEvents();
	}

	@RequestMapping(value = "/getCurrentEvent", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Event getCurrentEvent() {
		if (!eventService.getCurrentEvent().isEmpty()) {
			return eventService.getCurrentEvent().get(0);
		}
		return null;
	}

	@RequestMapping(value = "/changeEventState/{eventID}/{state}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void changeEventState(@PathVariable("eventID") final int eventID,
			@PathVariable("state") final String state) {
		eventService.changeEventState(eventID, state);
		messagingTemplate.convertAndSend("/topic/broadcastLatestComments",
				eventResponseService.getLatestResponses(eventID));
		if (eventService.getCurrentEvent() != null) {
			messagingTemplate.convertAndSend("/topic/broadcastCurrentEvent", eventService.getCurrentEvent());
		} else {
			messagingTemplate.convertAndSend("/topic/broadcastCurrentEvent", "");
		}
	}

	@RequestMapping(value = "/closeAllEvents", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void closeAllEvents() {
		eventService.closeAllEvents();
		messagingTemplate.convertAndSend("/topic/broadcastCurrentEvent", "");
	}

	@RequestMapping(value = "/saveEventResponse/{employeeID}/{eventID}/{vote}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void saveEventResponse(@PathVariable("employeeID") final String employeeID,
			@PathVariable("eventID") final int eventID, @PathVariable("vote") final String vote) {
		if (!eventService.getCurrentEvent().isEmpty()
				&& eventService.getCurrentEvent().get(0).getEventID() == eventID) {
			eventResponseService.save(Long.parseLong(employeeID), eventID, vote);
			messagingTemplate.convertAndSend("/topic/broadcastLatestComments",
					eventResponseService.getLatestResponses(eventID));
		}
	}

	@RequestMapping(value = "/saveEventResponseWithComment/{employeeID}/{eventID}/{vote}/{comment}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void saveEventResponseWithComment(@PathVariable("employeeID") final String employeeID,
			@PathVariable("eventID") final int eventID, @PathVariable("vote") final String vote,
			@PathVariable("comment") final String comment) {
		final List<Event> allRunningEvents = eventService.getCurrentEvent();
		if (null != allRunningEvents && !allRunningEvents.isEmpty()) {

			for (final Event event : allRunningEvents) {
				if (event.getEventID() == eventID) {
					eventResponseService.saveWithComment(Long.parseLong(employeeID), eventID, vote, comment);
					break;
				}
			}

			messagingTemplate.convertAndSend("/topic/broadcastLatestComments",
					eventResponseService.getLatestResponses(eventID));
		}
	}

	@MessageMapping("/getEventStatus")
	public void sendMessage(final Principal principal, @SuppressWarnings("rawtypes") final Map message) {
		if (eventService.getCurrentEvent() != null) {
			messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/getCurrentEvent",
					eventService.getCurrentEvent());
		} else {
			messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/getCurrentEvent", "");
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