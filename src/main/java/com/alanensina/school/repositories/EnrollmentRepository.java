package com.alanensina.school.repositories;

public interface EnrollmentRepository {

    void enrollStudent(Long courseId, Long studentId);
    void unEnrollStudent(Long courseId, Long studentId);

}
