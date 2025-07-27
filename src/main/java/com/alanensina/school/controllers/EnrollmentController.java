package com.alanensina.school.controllers;

import com.alanensina.school.services.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @PostMapping("/enroll")
    public ResponseEntity<Void> enroll(@RequestParam Long courseId, @RequestParam Long studentId){
        return service.enroll(courseId, studentId, true);
    }

    @PostMapping("/unenroll")
    public ResponseEntity<Void> unenroll(@RequestParam Long courseId, @RequestParam Long studentId){
        return service.enroll(courseId, studentId, false);
    }
}
