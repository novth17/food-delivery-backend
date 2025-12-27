package com.lilyhien.repository;

import com.lilyhien.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findAddressById(Long id);
}
