package com.tcs.novia.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name = "EmailTracker")
@Table(name = "EMAIL_TRACKER")
public class EmailTracker {

	@Id
	@GeneratedValue
	private int id;
	@Column(name = "EMAIL_TYPE", nullable = false)
	private String emailContext;
	@Column(name = "EMAIL_TEMPLATE_NAME", nullable = true)
	private String emailTemplateName;
	@Column(name = "DELIVERED", nullable = false)
	private boolean delivered;
	@Column(name = "FAILURE_REASON", nullable = true, length = 10485760)
	private String failureReason;
	@Column(name = "DATE_TIME_STAMP", nullable = false)
	private LocalDateTime dateTimeStamp;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMP_ID", nullable = false)
	@JsonIgnore
	private Employee employee;

	public EmailTracker() {
		super();
	}

	public EmailTracker(String emailContext, String emailTemplateName, boolean delivered, String failureReason,
			LocalDateTime dateTimeStamp, Employee employee) {
		super();
		this.emailContext = emailContext;
		this.emailTemplateName = emailTemplateName;
		this.delivered = delivered;
		this.failureReason = failureReason;
		this.dateTimeStamp = dateTimeStamp;
		this.employee = employee;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmailContext() {
		return emailContext;
	}

	public void setEmailContext(String emailContext) {
		this.emailContext = emailContext;
	}

	public String getEmailTemplateName() {
		return emailTemplateName;
	}

	public void setEmailTemplateName(String emailTemplateName) {
		this.emailTemplateName = emailTemplateName;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public String getFailureReason() {
		return failureReason;
	}

	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}

	public LocalDateTime getDateTimeStamp() {
		return dateTimeStamp;
	}

	public void setDateTimeStamp(LocalDateTime dateTimeStamp) {
		this.dateTimeStamp = dateTimeStamp;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "EmailTracker [id=" + id + ", emailContext=" + emailContext + ", emailTemplateName=" + emailTemplateName
				+ ", delivered=" + delivered + ", failureReason=" + failureReason + ", dateTimeStamp=" + dateTimeStamp
				+ ", employee=" + employee.getEmployeeID() + "]";
	}

}
