package com.alanensina.school.services;

import com.alanensina.school.dtos.course.CourseResponseDTO;
import com.alanensina.school.jooq.generated.tables.records.CourseRecord;
import com.alanensina.school.repositories.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CourseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public ResponseEntity<Void> create(String name, Long teacherId) {
        try{
            courseRepository.createCourse(
                    name,
                    teacherId
            );
        } catch (Exception e) {
            LOGGER.error("Error to save the Course. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<CourseResponseDTO> update(Long id, String name, Long teacherId) {
        try{
            var oldRecord = courseRepository.getById(id);

            if(oldRecord == null){
                LOGGER.error("Course not found. Id: {}", id);
                return ResponseEntity.badRequest().build();
            }

            courseRepository.updateCourse(id, name, teacherId);

            return getCourseById(id);
        } catch (Exception e) {
            LOGGER.error("Error to update the Teacher. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<CourseResponseDTO> getCourseById(Long id) {
        CourseRecord courseRecord;

        try{
            courseRecord = courseRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error("Error to find the Course. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        if(courseRecord == null){
            LOGGER.error("Course not found. Id: {}", id);
            return ResponseEntity.badRequest().build();
        }

        var response = new CourseResponseDTO(
                courseRecord.getId(),
                courseRecord.getName(),
                courseRecord.getTeacherId()
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<CourseResponseDTO>> list() {
        try{
            var courseRecords = courseRepository.findAll();

            if(courseRecords.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

            return ResponseEntity.ok(
                    courseRecords.stream().map(
                                    c -> new CourseResponseDTO(
                                            c.getId(),
                                            c.getName(),
                                            c.getTeacherId()
                                    ))
                            .toList()
            );
        } catch (Exception e) {
            LOGGER.error("Error to list all Courses. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Void> deleteById(Long id) {
        try{
            if(courseRepository.getById(id) == null){
                LOGGER.error("Teacher not found. Id: {}", id);
                return ResponseEntity.badRequest().build();
            }

            courseRepository.deleteCourse(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.error("Error to delete the Course. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<CourseResponseDTO>> findCourseByStudentId(Long studentId) {

        try{
            var coursesRecords = courseRepository.findCoursesByStudent(studentId);

            if(coursesRecords.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

            return ResponseEntity.ok(
                    coursesRecords.stream().map(c ->
                            new CourseResponseDTO(
                                    c.getId(),
                                    c.getName(),
                                    c.getTeacherId()
                            ))
                            .toList()
            );

        } catch (Exception e) {
            LOGGER.error("Error to list courses by student. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
