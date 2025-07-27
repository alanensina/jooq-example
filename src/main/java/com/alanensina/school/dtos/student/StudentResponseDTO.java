package com.alanensina.school.dtos.student;

public record StudentResponseDTO(
        Long id,
        String firstName,
        String lastName,
        int age,
        String email,
        String phone
) {
}
