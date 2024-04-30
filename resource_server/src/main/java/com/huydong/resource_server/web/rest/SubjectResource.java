package com.huydong.resource_server.web.rest;

import com.huydong.resource_server.domain.Subject;
import com.huydong.resource_server.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subject")
public class SubjectResource {
    private final SubjectService subjectService;

    public SubjectResource(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<Subject>> findSubject() {
        return ResponseEntity.ok(subjectService.getSubject());
    }
}
