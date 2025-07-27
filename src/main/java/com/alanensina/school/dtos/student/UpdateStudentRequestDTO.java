package com.alanensina.school.dtos.student;

public record UpdateStudentRequestDTO(
        Long id,
        String firstName,
        String lastName,
        int age,
        String email,
        String phone
) {
}
