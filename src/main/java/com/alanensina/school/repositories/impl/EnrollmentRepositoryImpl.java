package com.alanensina.school.repositories.impl;

import com.alanensina.school.repositories.EnrollmentRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.alanensina.school.jooq.generated.Tables.ENROLLMENT;

@Service
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    private final DSLContext dsl;

    public EnrollmentRepositoryImpl(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void enrollStudent(Long courseId, Long studentId) {
        dsl.insertInto(ENROLLMENT)
                .set(ENROLLMENT.COURSE_ID, courseId)
                .set(ENROLLMENT.STUDENT_ID, studentId)
                .onDuplicateKeyIgnore()
                .execute();
    }

    public void unEnrollStudent(Long courseId, Long studentId) {
        dsl.deleteFrom(ENROLLMENT)
                .where(ENROLLMENT.COURSE_ID.eq(courseId)
                        .and(ENROLLMENT.STUDENT_ID.eq(studentId)))
                .execute();
    }

}
