package com.alanensina.school.services;

import com.alanensina.school.dtos.student.CreateStudentRequestDTO;
import com.alanensina.school.dtos.student.StudentResponseDTO;
import com.alanensina.school.dtos.student.UpdateStudentRequestDTO;
import com.alanensina.school.jooq.generated.tables.records.StudentRecord;
import com.alanensina.school.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public ResponseEntity<Void> create(CreateStudentRequestDTO body) {
        try{
            studentRepository.createStudent(
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
        StudentRecord studentRecord;

        try{
            studentRecord = studentRepository.getById(id);
        } catch (Exception e) {
            LOGGER.error("Error to find the Student. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        if(studentRecord == null){
            LOGGER.error("Student not found. Id: {}", id);
            return ResponseEntity.badRequest().build();
        }

        var response = new StudentResponseDTO(
                studentRecord.getId(),
                studentRecord.getFirstname(),
                studentRecord.getLastname(),
                studentRecord.getAge(),
                studentRecord.getEmail(),
                studentRecord.getPhone()
        );

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<List<StudentResponseDTO>> list() {
        try{
            var studentsRecords = studentRepository.findAll();

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
            if(studentRepository.getById(id) == null){
                LOGGER.error("Student not found. Id: {}", id);
                return ResponseEntity.badRequest().build();
            }

            studentRepository.deleteStudent(id);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOGGER.error("Error to delete the Student. Error: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<StudentResponseDTO> update(UpdateStudentRequestDTO body) {
        try{
            var oldRecord = studentRepository.getById(body.id());

            if(oldRecord == null){
                LOGGER.error("Student not found. Id: {}", body.id());
                return ResponseEntity.badRequest().build();
            }

            studentRepository.updateStudent(
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
            var studentsRecords = studentRepository.listStudentsByCourse(id);

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
