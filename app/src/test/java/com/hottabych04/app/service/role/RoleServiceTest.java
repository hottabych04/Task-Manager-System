package com.hottabych04.app.service.role;

import com.hottabych04.app.database.entity.Role;
import com.hottabych04.app.database.repository.RoleRepository;
import com.hottabych04.app.exception.role.RoleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceTest {

    private static final String ROLE_PREFIX = "ROLE_";

    private RoleRepository roleRepository = Mockito.mock(RoleRepository.class);

    private RoleService roleService = new RoleService(roleRepository);

    @Test
    @DisplayName("Success get role")
    public void successGetRole(){
        String admin = "ADMIN";

        Role dummyRole = Role.builder()
                .id(1)
                .name(admin)
                .build();

        Mockito.when(roleRepository.getRoleByName(ROLE_PREFIX + admin)).thenReturn(Optional.of(dummyRole));

        Role role = roleService.getRole(admin);

        assertThat(role).isEqualTo(dummyRole);
    }

    @Test
    @DisplayName("Failed get role")
    public void failedGetRole(){
        String dummy = "dummy";

        Mockito.when(roleRepository.getRoleByName(ROLE_PREFIX + dummy)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.getRole(dummy));
    }
}
