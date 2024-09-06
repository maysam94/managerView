package com.internship.managerview.controllers.DTOs;

import java.util.List;

public record CreateTeamDTO(String name, List<Integer> members, int createdBy) {
}
