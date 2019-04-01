package com.tcs.novia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.EventResponse;
import com.tcs.novia.model.enums.Vote;

@Repository
public interface EventResponseRepository extends JpaRepository<EventResponse, String> {

	List<EventResponse> findTop7ByEventIDOrderByTimeStampDesc(int eventID);
	
	@Query("select count(vote) from EventResponse e where e.eventID=:eventID and e.vote=:vote group by eventID, vote")
	Integer getVoteCountForAnEvent(@Param("eventID") int eventID, @Param("vote") Vote vote);


}
