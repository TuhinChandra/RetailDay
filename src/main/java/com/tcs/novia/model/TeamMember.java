package com.tcs.novia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TEAM_MEMBER")
public class TeamMember {
	@Id
	@Column(name = "EMP_ID", nullable = false)
	private long employeeID;
	@Column(name = "EMAIL_ID", nullable = false)
	private String emailID;
	@Column(name = "FIRST_NAME", nullable = false)
	private String firstName;
	@Column(name = "LAST_NAME", nullable = false)
	private String lastName;
	@Column(name = "MOBILE", nullable = false)
	private String mobile;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "team_name", nullable = false)
	@JsonIgnore
	private SupportTeam supportTeam;

	public TeamMember() {

	}

	public TeamMember(final long employeeID, final String emailID, final String firstName, final String lastName,
			final String mobile) {
		this.employeeID = employeeID;
		this.emailID = emailID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
	}

	public TeamMember(final long employeeID, final String emailID, final String firstName, final String lastName,
			final String mobile, final SupportTeam supportTeam) {
		super();
		this.employeeID = employeeID;
		this.emailID = emailID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobile = mobile;
		this.supportTeam = supportTeam;
	}

	public long getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(final long employeeID) {
		this.employeeID = employeeID;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(final String emailID) {
		this.emailID = emailID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(final String mobile) {
		this.mobile = mobile;
	}

	public SupportTeam getSupportTeam() {
		return supportTeam;
	}

	public void setSupportTeam(final SupportTeam supportTeam) {
		this.supportTeam = supportTeam;
	}

}