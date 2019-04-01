package com.tcs.novia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.FlightInfo;

@Repository
public interface FlightRepository extends JpaRepository<FlightInfo, Integer> {

}
