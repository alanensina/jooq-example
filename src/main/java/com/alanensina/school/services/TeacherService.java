package com.alanensina.school.services;

import com.alanensina.school.dtos.teacher.CreateTeacherRequestDTO;
import com.alanensina.school.dtos.teacher.TeacherResponseDTO;
import com.alanensina.school.dtos.teacher.UpdateTeacherRequestDTO;
import com.alanensina.school.jooq.generated.tables.records.TeacherRecord;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.alanensina.school.jooq.generated.Tables.TEACHER;

@Service
public class TeacherService {

    private final DSLContext dsl;
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherService.class);

    public TeacherService(DSLContext dsl) {
        this.dsl = dsl;
    }

    private List<TeacherRecord> findAll() {
        return dsl.selectFrom(TEACHER).fetch();
    }

    private TeacherRecord getById(Long id){
       return dsl.selectFrom(TEACHER)
                .where(TEACHER.ID.eq(id))
                .fetchOne();
    }

    private void createTeacher(String firstName, String lastName, int age, String email, String phone) {
        var record = dsl.newRecord(TEACHER);
        record.setFirstname(firstName);
        record.setLastname(lastName);
        record.setAge(age);
        record.setEmail(email);
        record.setPhone(phone);
        record.store();
    }

    private void updateTeacher(Long id, String firstName, String lastName, int age, String email, String phone) {
         dsl.update(TEACHER)
                .set(TEACHER.FIRSTNAME, firstName)
                .set(TEACHER.LASTNAME, lastName)
                .set(TEACHER.AGE, age)
                .set(TEACHER.EMAIL, email)
                .set(TEACHER.PHONE, phone)
                .where(TEACHER.ID.eq(id))
                .execute();
    }

    private void deleteTeacher(Long id) {
        dsl.deleteFrom(TEACHER)
                .where(TEACHER.ID.eq(id))
                .execute();
    }

    public ResponseEntity<Void> create(CreateTeacherRequestDTO body) {
        try{
            createTeacher(
                    body.firstName(),
                    body.lastName(),
                    body.age(),
                    body.email(),
                    body.phone()
            );
        } catch (Exception e) {
            LOGGER.error("Error to save the Teacher. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<TeacherResponseDTO> getTeacherById(Long id) {
        TeacherRecord record;

        try{
            record = getById(id);
        } catch (Exception e) {
            LOGGER.error("Error to find the Teacher. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        if(record == null){
            LOGGER.error("Teacher not found. Id: {}", id);
            return ResponseEntity.badRequest().build();
        }

        var response = new TeacherResponseDTO(
                record.getId(),
                record.getFirstname(),
                record.getLastname(),
                record.getAge(),
                record.getEmail(),
                record.getPhone()
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<TeacherResponseDTO> updateTeacher(UpdateTeacherRequestDTO body) {

        try{
            var oldRecord = getById(body.id());

            if(oldRecord == null){
                LOGGER.error("Teacher not found. Id: {}", body.id());
                return ResponseEntity.badRequest().build();
            }

            updateTeacher(
                    body.id(),
                    body.firstName(),
                    body.lastName(),
                    body.age(),
                    body.email(),
                    body.phone()
            );

            return getTeacherById(body.id());
        } catch (Exception e) {
            LOGGER.error("Error to update the Teacher. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<List<TeacherResponseDTO>> list() {

        try{
            var teachersRecords = findAll();

            if(teachersRecords.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

            return ResponseEntity.ok(
                    teachersRecords.stream().map(
                            t -> new TeacherResponseDTO(
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
            LOGGER.error("Error to list all Teachers. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Void> deleteById(Long id) {

        try{
            if(getById(id) == null){
                LOGGER.error("Teacher not found. Id: {}", id);
                return ResponseEntity.badRequest().build();
            }

            deleteTeacher(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.error("Error to delete the Teacher. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
