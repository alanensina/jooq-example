package com.alanensina.school.services;

import com.alanensina.school.repositories.EnrollmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    private final CourseService courseService;
    private final StudentService studentService;
    private final EnrollmentRepository enrollmentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentService.class);

    public EnrollmentService(CourseService courseService, StudentService studentService, EnrollmentRepository enrollmentRepository) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.enrollmentRepository = enrollmentRepository;
    }

    public ResponseEntity<Void> enroll(Long courseId, Long studentId, boolean isEnroll) {
        try{
            var courseRecord = courseService.getCourseById(courseId);

            if(courseRecord == null){
                LOGGER.error("Course not found. Id: {}", courseId);
                return ResponseEntity.badRequest().build();
            }

            var studentRecord = studentService.getStudentById(studentId);

            if(studentRecord == null){
                LOGGER.error("Student not found. Id: {}", studentId);
                return ResponseEntity.badRequest().build();
            }

            if(isEnroll){
                enrollmentRepository.enrollStudent(courseId, studentId);
            }else{
                enrollmentRepository.unEnrollStudent(courseId, studentId);
            }

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            LOGGER.error("Error to enroll/unenroll student to the course. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
