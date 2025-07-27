package com.alanensina.school.dtos.teacher;

public record UpdateTeacherRequestDTO(
        Long id,
        String firstName,
        String lastName,
        int age,
        String email,
        String phone
) {
}
