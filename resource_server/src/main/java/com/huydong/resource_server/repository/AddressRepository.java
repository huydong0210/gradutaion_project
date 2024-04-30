package com.huydong.resource_server.repository;

import com.huydong.resource_server.domain.Address;
import com.huydong.resource_server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select a from Address a where  a.userId = ?1")
    Address findAddressByUserId(Long userId);
}
