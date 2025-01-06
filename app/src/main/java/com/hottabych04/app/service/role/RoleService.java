package com.hottabych04.app.service.role;

import com.hottabych04.app.database.entity.Role;
import com.hottabych04.app.database.repository.RoleRepository;
import com.hottabych04.app.exception.role.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class RoleService {

    private final static String ROLE_PREFIX = "ROLE_";

    private final RoleRepository roleRepository;

    public Role getRole(String role){
        return roleRepository.getRoleByName(roleWithPrefix(role))
                .orElseThrow(
                        () -> {
                            log.error("Role with name: " + role + " not found");
                            return new RoleNotFoundException("Role not found", role);
                        }
                );
    }

    public boolean isExist(String role){
        return roleRepository.existsByName(roleWithPrefix(role));
    }

    private String roleWithPrefix(String roleWithoutPrefix){
        return ROLE_PREFIX + roleWithoutPrefix;
    }
}
