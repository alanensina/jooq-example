package com.alanensina.school.dtos.student;

public record CreateStudentRequestDTO(
        String firstName,
        String lastName,
        int age,
        String email,
        String phone
) {
}
