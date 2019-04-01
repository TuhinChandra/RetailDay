package com.tcs.KingfisherDay.controller;

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
import com.tcs.KingfisherDay.model.Event;
import com.tcs.KingfisherDay.model.VoteCount;
import com.tcs.KingfisherDay.service.EventResponseService;
import com.tcs.KingfisherDay.service.EventService;

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
		if (!eventService.getCurrentEvent().isEmpty())
			return eventService.getCurrentEvent().get(0);
		return null;
	}

	@RequestMapping(value = "/changeEventState/{eventID}/{state}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void changeEventState(@PathVariable("eventID") int eventID, @PathVariable("state") String state) {
		eventService.changeEventState(eventID, state);
		messagingTemplate.convertAndSend("/topic/broadcastLatestComments",
				eventResponseService.getLatestResponses(eventID));
		if (eventService.getCurrentEvent() != null)
			messagingTemplate.convertAndSend("/topic/broadcastCurrentEvent", eventService.getCurrentEvent());
		else
			messagingTemplate.convertAndSend("/topic/broadcastCurrentEvent", "");
	}

	@RequestMapping(value = "/closeAllEvents", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void closeAllEvents() {
		eventService.closeAllEvents();
		messagingTemplate.convertAndSend("/topic/broadcastCurrentEvent", "");
	}

	@RequestMapping(value = "/saveEventResponse/{emailID}/{eventID}/{vote}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void saveEventResponse(@PathVariable("emailID") String emailID, @PathVariable("eventID") int eventID,
			@PathVariable("vote") String vote) {
		if (!eventService.getCurrentEvent().isEmpty()
				&& eventService.getCurrentEvent().get(0).getEventID() == eventID) {
			eventResponseService.save(emailID, eventID, vote);
			messagingTemplate.convertAndSend("/topic/broadcastLatestComments",
					eventResponseService.getLatestResponses(eventID));
		}
	}

	@RequestMapping(value = "/saveEventResponseWithComment/{emailID}/{eventID}/{vote}/{comment}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public void saveEventResponseWithComment(@PathVariable("emailID") String emailID,
			@PathVariable("eventID") int eventID, @PathVariable("vote") String vote,
			@PathVariable("comment") String comment) {
		List<Event> allRunningEvents=eventService.getCurrentEvent();
		if (null!=allRunningEvents && !allRunningEvents.isEmpty()) {
			
			for(Event event: allRunningEvents) {
				if(event.getEventID()==eventID) {
					eventResponseService.saveWithComment(emailID, eventID, vote, comment);
					break;
				}
			}
			
			messagingTemplate.convertAndSend("/topic/broadcastLatestComments",
					eventResponseService.getLatestResponses(eventID));
		}
	}

	@MessageMapping("/getEventStatus")
	public void sendMessage(Principal principal, @SuppressWarnings("rawtypes") Map message) {
		if (eventService.getCurrentEvent() != null)
			messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/getCurrentEvent",
					eventService.getCurrentEvent());
		else
			messagingTemplate.convertAndSendToUser(principal.getName(), "/topic/getCurrentEvent", "");
	}

	@RequestMapping(value = "/getVoteCountForAnEvent/{eventID}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public VoteCount getVoteCountForAnEvent(@PathVariable("eventID") int eventID) {
		return eventResponseService.getVoteCountForAnEvent(eventID, ""+eventID);
	}

	@RequestMapping(value = "/getVoteCountForAllEvents", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<VoteCount> getVoteCountForAllEvents() {
		List<VoteCount> voteCountForAllEvents=null;
		List<Event> allEvents=eventService.getAllEvents();
		if(null!=allEvents && !allEvents.isEmpty()) {
			voteCountForAllEvents=new ArrayList<>(allEvents.size());
			for(Event eachEvent:allEvents) {
				VoteCount voteCountOfAnEvent=eventResponseService.getVoteCountForAnEvent(eachEvent.getEventID(), eachEvent.getEventName());
				voteCountForAllEvents.add(voteCountOfAnEvent);
			}
		}
		
		return voteCountForAllEvents;
	}
}