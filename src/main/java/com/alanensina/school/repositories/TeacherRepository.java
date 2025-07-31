package com.alanensina.school.repositories;

import com.alanensina.school.jooq.generated.tables.records.TeacherRecord;

import java.util.List;

public interface TeacherRepository {

    List<TeacherRecord> findAll();
    TeacherRecord getById(Long id);
    void createTeacher(String firstName, String lastName, int age, String email, String phone);
    void updateTeacher(Long id, String firstName, String lastName, int age, String email, String phone);
    void deleteTeacher(Long id);
}
