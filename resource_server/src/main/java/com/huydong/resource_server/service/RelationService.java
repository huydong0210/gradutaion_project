package com.huydong.resource_server.service;

import com.huydong.resource_server.domain.Relation;
import com.huydong.resource_server.domain.User;
import com.huydong.resource_server.repository.RelationRepository;
import com.huydong.resource_server.repository.UserRepository;
import com.huydong.resource_server.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationService {
    private final RelationRepository relationRepository;
    private final UserRepository userRepository;

    public RelationService(RelationRepository relationRepository, UserRepository userRepository) {
        this.relationRepository = relationRepository;
        this.userRepository = userRepository;
    }
    public List<Relation> getRelations(){
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).orElseThrow(
            () -> new RuntimeException("user not existed")
        );
        return relationRepository.getRelationByUserId(user.getId());
    }
}
