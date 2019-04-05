package com.tcs.novia.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.Event;
import com.tcs.novia.model.enums.EventState;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	List<Event> findAllByOrderByEventID();
	
	List<Event> findByEventDateOrderByEventID(String eventDate);

	List<Event> findByState(EventState state);

	Event findByEventID(int eventID);

}