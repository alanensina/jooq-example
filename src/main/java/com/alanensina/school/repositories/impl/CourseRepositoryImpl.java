package com.alanensina.school.repositories.impl;

import com.alanensina.school.jooq.generated.Tables;
import com.alanensina.school.jooq.generated.tables.records.CourseRecord;
import com.alanensina.school.repositories.CourseRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.alanensina.school.jooq.generated.tables.Course.COURSE;

@Service
public class CourseRepositoryImpl implements CourseRepository {

    private final DSLContext dsl;

    public CourseRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<CourseRecord> findAll() {
        return dsl.selectFrom(COURSE).fetch();
    }


    public List<CourseRecord> findCoursesByStudent(Long studentId) {
        return dsl.select(Tables.COURSE.fields())
                .from(Tables.COURSE)
                .join(Tables.ENROLLMENT).on(Tables.COURSE.ID.eq(Tables.ENROLLMENT.COURSE_ID))
                .where(Tables.ENROLLMENT.STUDENT_ID.eq(studentId))
                .fetchInto(CourseRecord.class);
    }

    public CourseRecord getById(Long id){
        return dsl.selectFrom(COURSE)
                .where(COURSE.ID.eq(id))
                .fetchOne();
    }

    public void createCourse(String name, Long teacherId) {
        var courseRecord = dsl.newRecord(COURSE);
        courseRecord.setName(name);
        courseRecord.setTeacherId(teacherId);
        courseRecord.store();
    }

    public void updateCourse(Long id, String name, Long teacherId) {
        dsl.update(COURSE)
                .set(COURSE.NAME, name)
                .set(COURSE.TEACHER_ID, teacherId)
                .where(COURSE.ID.eq(id))
                .execute();
    }

    public void deleteCourse(Long id) {
        dsl.deleteFrom(COURSE)
                .where(COURSE.ID.eq(id))
                .execute();
    }
}
