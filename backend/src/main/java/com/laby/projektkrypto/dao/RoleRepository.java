package com.laby.projektkrypto.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.laby.projektkrypto.config.UserRole;
import com.laby.projektkrypto.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Integer>
{
    @Transactional(readOnly = true)
    Optional<Role> findByName(UserRole name);
}
