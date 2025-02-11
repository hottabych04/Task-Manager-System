package com.hottabych04.app.service.role;

import com.hottabych04.app.database.entity.Role;
import com.hottabych04.app.exception.role.RoleNotFoundException;
import com.hottabych04.app.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RoleServiceTest extends IntegrationTestBase {

    @Autowired
    private RoleService roleService;

    @Test
    @DisplayName("Success get role")
    public void successGetRole(){
        String admin = "ADMIN";

        Role role = roleService.getRole(admin);

        assertAll(() -> {
            assertThat(role).isNotNull();
            assertThat(role.getName().replace("ROLE_", "")).isNotEmpty().isEqualTo(admin);
        });
    }

    @Test
    @DisplayName("Failed get role")
    public void failedGetRole(){
        String dummy = "dummy";

        assertThrows(RoleNotFoundException.class, () -> roleService.getRole(dummy));
    }
}
