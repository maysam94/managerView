package com.internship.managerview.data.repositories;

import com.internship.managerview.data.entities.SecurityQuestion;
import org.springframework.data.repository.CrudRepository;

public interface SecurityQuestionRepository extends CrudRepository<SecurityQuestion, String> {
}
