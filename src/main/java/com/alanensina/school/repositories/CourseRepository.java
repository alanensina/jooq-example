package com.alanensina.school.repositories;

import com.alanensina.school.jooq.generated.tables.records.CourseRecord;

import java.util.List;

public interface CourseRepository {

    List<CourseRecord> findAll();
    List<CourseRecord> findCoursesByStudent(Long studentId);
    CourseRecord getById(Long id);
    void createCourse(String name, Long teacherId);
    void updateCourse(Long id, String name, Long teacherId);
    void deleteCourse(Long id);
}
