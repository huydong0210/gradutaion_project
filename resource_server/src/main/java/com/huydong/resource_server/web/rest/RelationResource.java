package com.huydong.resource_server.web.rest;

import com.huydong.resource_server.domain.Relation;
import com.huydong.resource_server.service.RelationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/relation")
public class RelationResource {
    private final RelationService relationService;

    public RelationResource(RelationService relationService) {
        this.relationService = relationService;
    }
    @GetMapping
    public ResponseEntity<List<Relation>> getRelation(){
        return ResponseEntity.ok(relationService.getRelations());
    }
}
