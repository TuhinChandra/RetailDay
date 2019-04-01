package com.tcs.KingfisherDay.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.tcs.KingfisherDay.model.Event;
import com.tcs.KingfisherDay.model.enums.EventState;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

	List<Event> findAllByOrderByEventID();

	List<Event> findByState(EventState state);

	Event findByEventID(int eventID);

}