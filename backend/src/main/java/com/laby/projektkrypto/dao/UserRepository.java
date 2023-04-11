package com.laby.projektkrypto.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.laby.projektkrypto.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Transactional(readOnly = true)
    Optional<User> findByUsername(String username);

    @Transactional(readOnly = true)
    Optional<User> findByEmail(String email);
}