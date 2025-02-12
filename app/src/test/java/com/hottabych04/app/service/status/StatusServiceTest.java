package com.hottabych04.app.service.status;

import com.hottabych04.app.database.entity.Status;
import com.hottabych04.app.database.repository.StatusRepository;
import com.hottabych04.app.exception.status.StatusNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class StatusServiceTest {

    private StatusRepository statusRepository = Mockito.mock(StatusRepository.class);

    private StatusService statusService = new StatusService(statusRepository);

    @Test
    @DisplayName("Success get status")
    public void successGetStatus() {
        String waitStatus = "WAIT";

        Status dummyStatus = Status.builder()
                .id(1)
                .name(waitStatus)
                .build();

        Mockito.when(statusRepository.findByName(waitStatus)).thenReturn(Optional.of(dummyStatus));

        Status status = statusService.getStatus(waitStatus);

        assertThat(status).isEqualTo(dummyStatus);
    }

    @Test
    @DisplayName("Failed get status")
    public void failedGetStatus(){
        String dummy = "dummy";

        Mockito.when(statusRepository.findByName(dummy)).thenReturn(Optional.empty());

        assertThrows(StatusNotFoundException.class, () -> statusService.getStatus(dummy));
    }
}
