package com.internship.managerview.data.repositories;

import com.internship.managerview.data.entities.Team;
import org.springframework.data.repository.CrudRepository;

public interface TeamRepository extends CrudRepository<Team, String> {
    Team findByName(String name);

    Iterable<Team> findTop100ByIsActiveTrueOrderByName();
}
