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
	@Column(name = "ARRIVAL_DATE", nullable = false)
	private String arrivalDate;
	@Column(name = "ARRIVAL_TIME", nullable = false)
	private String arrivalTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID", nullable = false)
	@JsonIgnore
	private Employee employee;

	public FlightInfo() {
		super();
	}

	public FlightInfo(final int id, final String airlineName, final String flightNumber, final FlightType flightType,
			final String arrivalDate, final String arrivalTime, final Employee employee) {
		super();
		this.id = id;
		this.airlineName = airlineName;
		this.flightNumber = flightNumber;
		this.flightType = flightType;
		this.arrivalDate = arrivalDate;
		this.arrivalTime = arrivalTime;
		this.employee = employee;
	}

	public FlightInfo(final String airlineName, final String flightNumber, final FlightType flightType,
			final String arrivalDate, final String arrivalTime, final Employee employee) {
		super();
		this.airlineName = airlineName;
		this.flightNumber = flightNumber;
		this.flightType = flightType;
		this.arrivalDate = arrivalDate;
		this.arrivalTime = arrivalTime;
		this.employee = employee;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(final String airlineName) {
		this.airlineName = airlineName;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(final String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public FlightType getFlightType() {
		return flightType;
	}

	public void setFlightType(final FlightType flightType) {
		this.flightType = flightType;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(final String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(final String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(final Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "FlightInfo [id=" + id + ", airlineName=" + airlineName + ", flightNumber=" + flightNumber
				+ ", flightType=" + flightType + ", arrivalDate=" + arrivalDate + ", arrivalTime=" + arrivalTime
				+ ", employee=" + employee + "]";
	}

}
