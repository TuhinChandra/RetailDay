package com.tcs.KingfisherDay.model;

public class EventResponseEnvelope {
	private EventResponse eventResponse;
	private Employee employee;

	public EventResponseEnvelope() {
	}

	public EventResponseEnvelope(EventResponse eventResponse, Employee employee) {
		super();
		this.eventResponse = eventResponse;
		this.employee = employee;
	}

	public EventResponse getEventResponse() {
		return eventResponse;
	}

	public void setEventResponse(EventResponse eventResponse) {
		this.eventResponse = eventResponse;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

}
