package com.alanensina.school.services;

import com.alanensina.school.dtos.student.CreateStudentRequestDTO;
import com.alanensina.school.dtos.student.StudentResponseDTO;
import com.alanensina.school.dtos.student.UpdateStudentRequestDTO;
import com.alanensina.school.jooq.generated.tables.Student;
import com.alanensina.school.jooq.generated.tables.records.StudentRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.alanensina.school.jooq.generated.Tables.STUDENT;
import static com.alanensina.school.jooq.generated.tables.Enrollment.ENROLLMENT;

@Service
public class StudentService {

    private final DSLContext dsl;
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    public StudentService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<StudentRecord> findAll() {
        return dsl.selectFrom(STUDENT).fetch();
    }

    private void createStudent(String firstName, String lastName, int age, String email, String phone) {
        var record = dsl.newRecord(STUDENT);
        record.setFirstname(firstName);
        record.setLastname(lastName);
        record.setAge(age);
        record.setEmail(email);
        record.setPhone(phone);
        record.store();
    }

    private void updateStudent(Long id, String firstName, String lastName, int age, String email, String phone) {
         dsl.update(STUDENT)
                .set(STUDENT.FIRSTNAME, firstName)
                .set(STUDENT.LASTNAME, lastName)
                .set(STUDENT.AGE, age)
                .set(STUDENT.EMAIL, email)
                .set(STUDENT.PHONE, phone)
                .where(STUDENT.ID.eq(id))
                .execute();
    }

    private void deleteStudent(Long id) {
        dsl.deleteFrom(STUDENT)
                .where(STUDENT.ID.eq(id))
                .execute();
    }

    private List<StudentRecord> listStudentsByCourse(Long courseId) {
        return dsl.select(Student.STUDENT.fields())
                .from(Student.STUDENT)
                .join(ENROLLMENT).on(Student.STUDENT.ID.eq(ENROLLMENT.STUDENT_ID))
                .where(ENROLLMENT.COURSE_ID.eq(courseId))
                .fetchInto(StudentRecord.class);
    }

    public StudentRecord getById(Long id){
        return dsl.selectFrom(STUDENT)
                .where(STUDENT.ID.eq(id))
                .fetchOne();
    }

    public ResponseEntity<Void> create(CreateStudentRequestDTO body) {
        try{
            createStudent(
                    body.firstName(),
                    body.lastName(),
                    body.age(),
                    body.email(),
                    body.phone()
            );
        } catch (Exception e) {
            LOGGER.error("Error to save the Student. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<StudentResponseDTO> getStudentById(Long id) {
        StudentRecord record;

        try{
            record = getById(id);
        } catch (Exception e) {
            LOGGER.error("Error to find the Student. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        if(record == null){
            LOGGER.error("Student not found. Id: {}", id);
            return ResponseEntity.badRequest().build();
        }

        var response = new StudentResponseDTO(
                record.getId(),
                record.getFirstname(),
                record.getLastname(),
                record.getAge(),
                record.getEmail(),
                record.getPhone()
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<StudentResponseDTO>> list() {
        try{
            var studentsRecords = findAll();

            if(studentsRecords.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

            return ResponseEntity.ok(
                    studentsRecords.stream().map(
                                    t -> new StudentResponseDTO(
                                            t.getId(),
                                            t.getFirstname(),
                                            t.getLastname(),
                                            t.getAge(),
                                            t.getEmail(),
                                            t.getPhone()
                                    ))
                            .toList()
            );
        } catch (Exception e) {
            LOGGER.error("Error to list all Students. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Void> deleteById(Long id) {
        try{
            if(getById(id) == null){
                LOGGER.error("Student not found. Id: {}", id);
                return ResponseEntity.badRequest().build();
            }

            deleteStudent(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.error("Error to delete the Student. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<StudentResponseDTO> update(UpdateStudentRequestDTO body) {
        try{
            var oldRecord = getById(body.id());

            if(oldRecord == null){
                LOGGER.error("Student not found. Id: {}", body.id());
                return ResponseEntity.badRequest().build();
            }

            updateStudent(
                    body.id(),
                    body.firstName(),
                    body.lastName(),
                    body.age(),
                    body.email(),
                    body.phone()
            );

            return getStudentById(body.id());
        } catch (Exception e) {
            LOGGER.error("Error to update the Student. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<StudentResponseDTO>> listStudentsByCourseId(Long id) {

        try {
            var studentsRecords = listStudentsByCourse(id);

            if (studentsRecords.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

            return ResponseEntity.ok(
                    studentsRecords
                            .stream()
                            .map(s ->
                                    new StudentResponseDTO(
                                            s.getId(),
                                            s.getFirstname(),
                                            s.getLastname(),
                                            s.getAge(),
                                            s.getEmail(),
                                            s.getPhone()
                                    ))
                            .toList()
            );
        } catch (Exception e) {
            LOGGER.error("Error to list all students from the Course. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
