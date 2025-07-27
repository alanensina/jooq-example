package com.alanensina.school.services;

import com.alanensina.school.dtos.course.CourseResponseDTO;
import com.alanensina.school.jooq.generated.Tables;
import com.alanensina.school.jooq.generated.tables.records.CourseRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.alanensina.school.jooq.generated.tables.Course.COURSE;

@Service
public class CourseService {

    private final DSLContext dsl;
    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);

    public CourseService(DSLContext dsl) {
        this.dsl = dsl;
    }

    private List<CourseRecord> findAll() {
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

    private void createCourse(String name, Long teacherId) {
        var record = dsl.newRecord(COURSE);
        record.setName(name);
        record.setTeacherId(teacherId);
        record.store();
    }

    private void updateCourse(Long id, String name, Long teacherId) {
         dsl.update(COURSE)
                .set(COURSE.NAME, name)
                .set(COURSE.TEACHER_ID, teacherId)
                .where(COURSE.ID.eq(id))
                .execute();
    }

    private void deleteCourse(Long id) {
         dsl.deleteFrom(COURSE)
                .where(COURSE.ID.eq(id))
                .execute();
    }

    public ResponseEntity<Void> create(String name, Long teacherId) {
        try{
            createCourse(
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
            var oldRecord = getById(id);

            if(oldRecord == null){
                LOGGER.error("Course not found. Id: {}", id);
                return ResponseEntity.badRequest().build();
            }

            updateCourse(id, name, teacherId);

            return getCourseById(id);
        } catch (Exception e) {
            LOGGER.error("Error to update the Teacher. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<CourseResponseDTO> getCourseById(Long id) {
        CourseRecord record;

        try{
            record = getById(id);
        } catch (Exception e) {
            LOGGER.error("Error to find the Course. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        if(record == null){
            LOGGER.error("Course not found. Id: {}", id);
            return ResponseEntity.badRequest().build();
        }

        var response = new CourseResponseDTO(
                record.getId(),
                record.getName(),
                record.getTeacherId()
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<CourseResponseDTO>> list() {
        try{
            var courseRecords = findAll();

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
            if(getById(id) == null){
                LOGGER.error("Teacher not found. Id: {}", id);
                return ResponseEntity.badRequest().build();
            }

            deleteCourse(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.error("Error to delete the Course. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<CourseResponseDTO>> findCourseByStudentId(Long studentId) {

        try{
            var coursesRecords = findCoursesByStudent(studentId);

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
