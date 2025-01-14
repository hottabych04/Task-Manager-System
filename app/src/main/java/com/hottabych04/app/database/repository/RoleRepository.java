package com.hottabych04.app.database.repository;

import com.hottabych04.app.database.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> getRoleByName(String name);

}
