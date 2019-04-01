package com.tcs.novia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcs.novia.model.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {

}
