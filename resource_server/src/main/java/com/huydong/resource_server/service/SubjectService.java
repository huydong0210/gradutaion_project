package com.huydong.resource_server.service;

import com.huydong.resource_server.domain.Subject;
import com.huydong.resource_server.domain.User;
import com.huydong.resource_server.repository.SubjectRepository;
import com.huydong.resource_server.repository.UserRepository;
import com.huydong.resource_server.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public SubjectService(UserRepository userRepository, SubjectRepository subjectRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    }
    public List<Subject> getSubject(){
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).orElseThrow(
            () -> new RuntimeException("user not existed")
        );
        List<Subject> subjects = subjectRepository.findSubjectByUserId(user.getId());
        return subjects;
    }
}
