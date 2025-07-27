package com.alanensina.school.controllers;

import com.alanensina.school.dtos.teacher.CreateTeacherRequestDTO;
import com.alanensina.school.dtos.teacher.TeacherResponseDTO;
import com.alanensina.school.dtos.teacher.UpdateTeacherRequestDTO;
import com.alanensina.school.services.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    private final TeacherService service;

    public TeacherController(TeacherService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateTeacherRequestDTO body){
        return service.create(body);
    }

    @GetMapping
    public ResponseEntity<TeacherResponseDTO> getById(@RequestParam Long id){
        return service.getTeacherById(id);
    }

    @PutMapping
    public ResponseEntity<TeacherResponseDTO> update(@RequestBody UpdateTeacherRequestDTO body){
        return service.updateTeacher(body);
    }

    @GetMapping("/list")
    public ResponseEntity<List<TeacherResponseDTO>> listAll(){
        return service.list();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id){
        return service.deleteById(id);
    }
}
