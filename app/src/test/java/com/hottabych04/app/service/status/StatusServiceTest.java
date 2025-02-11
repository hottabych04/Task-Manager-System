package com.hottabych04.app.service.status;

import com.hottabych04.app.database.entity.Status;
import com.hottabych04.app.exception.status.StatusNotFoundException;
import com.hottabych04.app.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class StatusServiceTest extends IntegrationTestBase {

    @Autowired
    private StatusService statusService;

    @Test
    @DisplayName("Success get status")
    public void successGetStatus() {
        String waitStatus = "WAIT";

        Status status = statusService.getStatus(waitStatus);

        assertAll(() -> {
            assertThat(status).isNotNull();
            assertThat(status.getName()).isEqualTo(waitStatus);
        });
    }

    @Test
    @DisplayName("Failed get status")
    public void failedGetStatus(){
        String dummy = "dummy";

        assertThrows(StatusNotFoundException.class, () -> statusService.getStatus(dummy));
    }
}
