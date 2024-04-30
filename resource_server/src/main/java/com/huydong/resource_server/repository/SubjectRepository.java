package com.huydong.resource_server.repository;

import com.huydong.resource_server.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("select s from Subject s where s.userId = ?1 ")
    List<Subject> findSubjectByUserId(Long userId);
}
