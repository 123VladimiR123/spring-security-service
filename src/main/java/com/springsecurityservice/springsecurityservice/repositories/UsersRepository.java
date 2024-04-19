package com.springsecurityservice.springsecurityservice.repositories;

import com.springsecurityservice.springsecurityservice.entities.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<CustomUser, UUID > {

    CustomUser findUserByEmail (String email);

    CustomUser findCustomUserByIdAndEmail(UUID id, String email);
}
