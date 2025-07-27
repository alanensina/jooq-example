package com.alanensina.school.controllers;

import com.alanensina.school.dtos.student.CreateStudentRequestDTO;
import com.alanensina.school.dtos.student.StudentResponseDTO;
import com.alanensina.school.dtos.student.UpdateStudentRequestDTO;
import com.alanensina.school.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateStudentRequestDTO body){
        return service.create(body);
    }

    @GetMapping
    public ResponseEntity<StudentResponseDTO> getById(@RequestParam Long id){
        return service.getStudentById(id);
    }

    @PutMapping
    public ResponseEntity<StudentResponseDTO> update(@RequestBody UpdateStudentRequestDTO body){
        return service.update(body);
    }

    @GetMapping("/list")
    public ResponseEntity<List<StudentResponseDTO>> listAll(){
        return service.list();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id){
        return service.deleteById(id);
    }

    @GetMapping("/course")
    public ResponseEntity<List<StudentResponseDTO>> listStudentsByCourse(@RequestParam Long id){
        return service.listStudentsByCourseId(id);
    }
}
