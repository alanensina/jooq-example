package com.alanensina.school.controllers;

import com.alanensina.school.dtos.course.CourseResponseDTO;
import com.alanensina.school.services.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestParam String name,
                                       @RequestParam Long teacherId){
        return service.create(name, teacherId);
    }

    @PutMapping
    public ResponseEntity<CourseResponseDTO> update(@RequestParam Long id,
                                                    @RequestParam String name,
                                                    @RequestParam Long teacherId){
        return service.update(id, name, teacherId);
    }

    @GetMapping
    public ResponseEntity<CourseResponseDTO> getById(@RequestParam Long id){
        return service.getCourseById(id);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CourseResponseDTO>> listAll(){
        return service.list();
    }

    @GetMapping("/student")
    public ResponseEntity<List<CourseResponseDTO>> findCourseByStudent(@RequestParam Long studentId){
        return service.findCourseByStudentId(studentId);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam Long id){
        return service.deleteById(id);
    }

}
