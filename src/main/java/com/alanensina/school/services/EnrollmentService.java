package com.alanensina.school.services;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.alanensina.school.jooq.generated.Tables.*;

@Service
public class EnrollmentService {

    private final DSLContext dsl;
    private final CourseService courseService;
    private final StudentService studentService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentService.class);

    public EnrollmentService(DSLContext dsl, CourseService courseService, StudentService studentService) {
        this.dsl = dsl;
        this.courseService = courseService;
        this.studentService = studentService;
    }

    private void enrollStudent(Long courseId, Long studentId) {
        dsl.insertInto(ENROLLMENT)
                .set(ENROLLMENT.COURSE_ID, courseId)
                .set(ENROLLMENT.STUDENT_ID, studentId)
                .onDuplicateKeyIgnore()
                .execute();
    }

    private void unenrollStudent(Long courseId, Long studentId) {
        dsl.deleteFrom(ENROLLMENT)
                .where(ENROLLMENT.COURSE_ID.eq(courseId)
                        .and(ENROLLMENT.STUDENT_ID.eq(studentId)))
                .execute();
    }

    public ResponseEntity<Void> enroll(Long courseId, Long studentId, boolean isEnroll) {
        try{
            var courseRecord = courseService.getById(courseId);

            if(courseRecord == null){
                LOGGER.error("Course not found. Id: {}", courseId);
                return ResponseEntity.badRequest().build();
            }

            var studentRecord = studentService.getById(courseId);

            if(studentRecord == null){
                LOGGER.error("Student not found. Id: {}", studentId);
                return ResponseEntity.badRequest().build();
            }

            if(isEnroll){
                enrollStudent(courseId, studentId);
            }else{
                unenrollStudent(courseId, studentId);
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            LOGGER.error("Error to enroll/unenroll student to the course. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
