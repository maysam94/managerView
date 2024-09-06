package com.internship.managerview.controllers.DTOs;

import java.util.Date;
import java.util.List;

public record TeamDetailsDTO(String name, List<UserNameAndIdDTO> members, Date createdAt) {
}
