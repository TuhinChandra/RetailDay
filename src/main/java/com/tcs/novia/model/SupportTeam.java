package com.tcs.novia.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "SUPPORT_TEAM")
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportTeam {
	@Id
	@Column(name = "team_name", nullable = false)
	private String teamName;
	@Column(name = "team_description", nullable = false)
	private String description;

	@OneToMany(mappedBy = "supportTeam", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<TeamMember> teamMembers;

	public SupportTeam() {

	}

	public SupportTeam(final String teamName, final String description, final List<TeamMember> teamMembers) {
		this.teamName = teamName;
		this.description = description;
		this.teamMembers = teamMembers;
	}

	public SupportTeam(final String teamName, final String description) {
		super();
		this.teamName = teamName;
		this.description = description;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(final String teamName) {
		this.teamName = teamName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public List<TeamMember> getTeamMembers() {
		return teamMembers;
	}

	public void setTeamMembers(final List<TeamMember> teamMembers) {
		this.teamMembers = teamMembers;
	}

}