package com.huydong.resource_server.service;

import com.huydong.resource_server.domain.Address;
import com.huydong.resource_server.domain.User;
import com.huydong.resource_server.repository.AddressRepository;
import com.huydong.resource_server.repository.UserRepository;
import com.huydong.resource_server.security.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }
    public Address getAddress(){
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get()).orElseThrow(
            () -> new RuntimeException("user not existed")
        );
        return addressRepository.findAddressByUserId(user.getId());
    }
}
