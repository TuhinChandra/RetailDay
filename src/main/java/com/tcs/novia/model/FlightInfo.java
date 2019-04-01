package com.tcs.novia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcs.novia.model.enums.FlightType;

@Entity(name = "FlightInfo")
@Table(name = "FLIGHT_INFO")
public class FlightInfo {

	@Id
	@GeneratedValue
	private int id;
	@Column(name = "AIRLINE_NAME", nullable = false)
	private String airlineName;
	@Column(name = "FLIGHT_NUMBER", nullable = false)
	private String flightNumber;
	@Column(name = "FLIGHT_TYPE", nullable = false)
	private FlightType flightType;
	@Column(name = "FLIGHT_DATE", nullable = false)
	private String flightDate;
	@Column(name = "FLIGHT_TIME", nullable = false)
	private String flightTime;
	@Column(name = "LOCATION", nullable = false)
	private String location;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID", nullable = false)
	@JsonIgnore
	private Employee employee;

	public FlightInfo() {
		super();
	}

	public FlightInfo(String airlineName, String flightNumber, FlightType flightType, String flightDate,
			String flightTime, String location, Employee employee) {
		super();
		this.airlineName = airlineName;
		this.flightNumber = flightNumber;
		this.flightType = flightType;
		this.flightDate = flightDate;
		this.flightTime = flightTime;
		this.location = location;
		this.employee = employee;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public FlightType getFlightType() {
		return flightType;
	}

	public void setFlightType(FlightType flightType) {
		this.flightType = flightType;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}

	public String getFlightTime() {
		return flightTime;
	}

	public void setFlightTime(String flightTime) {
		this.flightTime = flightTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "FlightInfo [id=" + id + ", airlineName=" + airlineName + ", flightNumber=" + flightNumber
				+ ", flightType=" + flightType + ", flightDate=" + flightDate + ", flightTime=" + flightTime
				+ ", location=" + location + "]";
	}

}
