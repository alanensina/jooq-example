package com.alanensina.school.dtos.teacher;

public record TeacherResponseDTO(
        Long id,
        String firstName,
        String lastName,
        int age,
        String email,
        String phone
) {
}
