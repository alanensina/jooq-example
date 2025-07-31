package com.alanensina.school.services;

import com.alanensina.school.dtos.teacher.CreateTeacherRequestDTO;
import com.alanensina.school.dtos.teacher.TeacherResponseDTO;
import com.alanensina.school.dtos.teacher.UpdateTeacherRequestDTO;
import com.alanensina.school.jooq.generated.tables.records.TeacherRecord;
import com.alanensina.school.repositories.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherService.class);

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public ResponseEntity<Void> create(CreateTeacherRequestDTO body) {
        try{
            teacherRepository.createTeacher(
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
        TeacherRecord teacherRecord;

        try{
            teacherRecord = teacherRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error("Error to find the Teacher. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        if(teacherRecord == null){
            LOGGER.error("Teacher not found. Id: {}", id);
            return ResponseEntity.badRequest().build();
        }

        var response = new TeacherResponseDTO(
                teacherRecord.getId(),
                teacherRecord.getFirstname(),
                teacherRecord.getLastname(),
                teacherRecord.getAge(),
                teacherRecord.getEmail(),
                teacherRecord.getPhone()
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<TeacherResponseDTO> updateTeacher(UpdateTeacherRequestDTO body) {

        try{
            var oldRecord = teacherRepository.getById(body.id());

            if(oldRecord == null){
                LOGGER.error("Teacher not found. Id: {}", body.id());
                return ResponseEntity.badRequest().build();
            }

            teacherRepository.updateTeacher(
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
            var teachersRecords = teacherRepository.findAll();

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
            if(teacherRepository.getById(id) == null){
                LOGGER.error("Teacher not found. Id: {}", id);
                return ResponseEntity.badRequest().build();
            }

            teacherRepository.deleteTeacher(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.error("Error to delete the Teacher. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
