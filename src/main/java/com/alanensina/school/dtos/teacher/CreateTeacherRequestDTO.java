package com.alanensina.school.dtos.teacher;

public record CreateTeacherRequestDTO(
        String firstName,
        String lastName,
        int age,
        String email,
        String phone
) {
}
