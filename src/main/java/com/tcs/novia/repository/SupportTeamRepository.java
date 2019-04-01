package com.tcs.novia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.SupportTeam;

@Repository
public interface SupportTeamRepository extends JpaRepository<SupportTeam, String> {

}
