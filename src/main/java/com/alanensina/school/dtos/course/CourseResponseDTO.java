package com.alanensina.school.dtos.course;

public record CourseResponseDTO(
        Long id,
        String name,
        Long teacherId
) {
}
