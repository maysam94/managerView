package com.internship.managerview.data.repositories;

import com.internship.managerview.data.entities.Image;
import org.springframework.data.repository.CrudRepository;

public interface ImageRepository extends CrudRepository<Image, String> {
}
