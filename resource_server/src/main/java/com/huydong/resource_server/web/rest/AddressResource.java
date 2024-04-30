package com.huydong.resource_server.web.rest;

import com.huydong.resource_server.domain.Address;
import com.huydong.resource_server.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/address")
public class AddressResource {
    private final AddressService addressService;

    public AddressResource(AddressService addressService) {
        this.addressService = addressService;
    }
    @GetMapping
    public ResponseEntity<Address> getAddress(){
        return ResponseEntity.ok(addressService.getAddress());
    }
}
