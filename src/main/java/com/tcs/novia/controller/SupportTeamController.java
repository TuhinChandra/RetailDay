package com.tcs.novia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tcs.novia.model.SupportTeam;
import com.tcs.novia.model.TeamMember;
import com.tcs.novia.repository.SupportTeamRepository;
import com.tcs.novia.repository.TeamMemberRepository;

@Controller
public class SupportTeamController {

	@Autowired
	protected SupportTeamRepository supportTeamRepository;
	@Autowired
	protected TeamMemberRepository teamMemberRepository;

	@RequestMapping(value = "/createSupportTeam/{teamName}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public SupportTeam createEmployee(@PathVariable("teamName") final String teamName,
			@RequestParam(value = "description", required = false) final String description) {

		final SupportTeam supportTeam = new SupportTeam(teamName, description);

		supportTeamRepository.save(supportTeam);

		return supportTeam;

	}

	@RequestMapping(value = "/addTeamMember/{teamName}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public SupportTeam createTeamMember(@PathVariable("teamName") final String teamName,
			@RequestParam(value = "employeeID", required = false) final long employeeID,
			@RequestParam(value = "emailID", required = false) final String emailID,
			@RequestParam(value = "firstName", required = false) final String firstName,
			@RequestParam(value = "lastName", required = false) final String lastName,
			@RequestParam(value = "mobile", required = false) final String mobile) {

		SupportTeam supportTeam = null;
		final Optional<SupportTeam> optionalSupportTeam = supportTeamRepository.findById(teamName);
		if (optionalSupportTeam.isPresent()) {
			supportTeam = optionalSupportTeam.get();
			final TeamMember teamMember = new TeamMember(employeeID, emailID, firstName, lastName, mobile, supportTeam);
			supportTeam.getTeamMembers().add(teamMember);
			supportTeamRepository.save(supportTeam);
		}

		return supportTeam;

	}

	@RequestMapping(value = "/removeTeamMember/{teamName}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public SupportTeam removeTeamMember(@PathVariable("teamName") final String teamName,
			@RequestParam(value = "employeeID", required = false) final long employeeID) {

		SupportTeam supportTeam = null;
		TeamMember teamMember = null;
		final Optional<SupportTeam> optionalSupportTeam = supportTeamRepository.findById(teamName);
		if (optionalSupportTeam.isPresent()) {
			supportTeam = optionalSupportTeam.get();
			final Optional<TeamMember> optionalTeamMember = teamMemberRepository.findById(employeeID);
			if (optionalTeamMember.isPresent()) {
				teamMember = optionalTeamMember.get();
				if (supportTeam.getTeamMembers().contains(teamMember)) {
					supportTeam.getTeamMembers().remove(teamMember);
					teamMemberRepository.delete(teamMember);
					supportTeamRepository.save(supportTeam);
				}
			}
		}
		return supportTeam;

	}

	@RequestMapping(value = "/removeSupportTeam/{teamName}", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public void removeSupportTeam(@PathVariable("teamName") final String teamName) {
		supportTeamRepository.deleteById(teamName);

	}

	@RequestMapping(value = "/getSupportTeam/{teamName}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public SupportTeam getAllSupportTeams(@PathVariable("teamName") final String teamName) {
		SupportTeam supportTeam = null;
		final Optional<SupportTeam> optionalSupportTeam = supportTeamRepository.findById(teamName);
		if (optionalSupportTeam.isPresent()) {
			supportTeam = optionalSupportTeam.get();
		}
		return supportTeam;
	}

	@RequestMapping(value = "/getAllSupportTeams/", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<SupportTeam> getAllSupportTeams() {
		return supportTeamRepository.findAll();
	}

}
