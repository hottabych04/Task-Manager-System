package com.hottabych04.example.service.priority;

import com.hottabych04.app.database.entity.Priority;
import com.hottabych04.app.exception.priority.PriorityNotFoundException;
import com.hottabych04.app.service.priority.PriorityService;
import com.hottabych04.example.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PriorityServiceTest extends IntegrationTestBase {

    @Autowired
    private PriorityService priorityService;

    @Test
    @DisplayName("Success get priority")
    public void successGetPriority(){
        String lowPriority = "LOW";

        Priority priority = priorityService.getPriority(lowPriority);

        assertAll(() -> {
            assertThat(priority).isNotNull();
            assertThat(priority.getName()).isEqualTo(lowPriority);
        });
    }

    @Test
    @DisplayName("Success get priority")
    public void failedGetPriority(){
        String dummy = "dummy";

        assertThrows(PriorityNotFoundException.class, () -> priorityService.getPriority(dummy));
    }
}
