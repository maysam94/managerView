package com.internship.managerview.controllers.DTOs;

import java.util.List;

public record UpdateTeamDTO(String name, List<Integer> members) {
}
