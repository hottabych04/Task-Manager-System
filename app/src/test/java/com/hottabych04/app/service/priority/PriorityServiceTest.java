package com.hottabych04.app.service.priority;

import com.hottabych04.app.database.entity.Priority;
import com.hottabych04.app.database.repository.PriorityRepository;
import com.hottabych04.app.exception.priority.PriorityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class PriorityServiceTest {

    private PriorityRepository priorityRepository = Mockito.mock(PriorityRepository.class);

    private PriorityService priorityService = new PriorityService(priorityRepository);

    @Test
    @DisplayName("Success get priority")
    public void successGetPriority(){
        String lowPriority = "LOW";

        Priority dummyPriority = Priority.builder()
                .id(1)
                .name(lowPriority)
                .build();

        Mockito.when(priorityRepository.findByName(lowPriority)).thenReturn(Optional.of(dummyPriority));

        Priority priority = priorityService.getPriority(lowPriority);

        assertThat(priority).isEqualTo(dummyPriority);
    }

    @Test
    @DisplayName("Success get priority")
    public void failedGetPriority(){
        String dummy = "dummy";

        Mockito.when(priorityRepository.findByName(dummy)).thenReturn(Optional.empty());

        assertThrows(PriorityNotFoundException.class, () -> priorityService.getPriority(dummy));
    }
}
