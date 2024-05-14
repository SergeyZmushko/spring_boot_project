package org.home.manager.repository;

import org.home.manager.entity.CustomUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomUserRepository extends CrudRepository<CustomUser, Integer> {

    Optional<CustomUser> findByUserName(String userName);
}
