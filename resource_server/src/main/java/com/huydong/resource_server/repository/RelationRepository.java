package com.huydong.resource_server.repository;

import com.huydong.resource_server.domain.Relation;
import liquibase.pro.packaged.Q;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    @Query("select r from  Relation r where r.userId = ?1 ")
    List<Relation> getRelationByUserId(Long userId);
}
