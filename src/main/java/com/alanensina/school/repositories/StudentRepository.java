package com.alanensina.school.repositories;

import com.alanensina.school.jooq.generated.tables.records.StudentRecord;

import java.util.List;

public interface StudentRepository {

    List<StudentRecord> findAll();
    void createStudent(String firstName, String lastName, int age, String email, String phone);
    void updateStudent(Long id, String firstName, String lastName, int age, String email, String phone);
    void deleteStudent(Long id);
    List<StudentRecord> listStudentsByCourse(Long courseId);
    StudentRecord getById(Long id);
}
