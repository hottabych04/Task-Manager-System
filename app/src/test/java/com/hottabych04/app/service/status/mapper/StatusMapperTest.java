package com.hottabych04.app.service.status.mapper;

import com.hottabych04.app.database.entity.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StatusMapperTest {

    private StatusMapper statusMapper = new StatusMapperImpl();

    @Test
    @DisplayName("Success map status entity to string")
    public void statusEntityToString(){
        Status dummy = new Status(1, "DUMMY");
        String statusString = statusMapper.toStatusString(dummy);

        assertThat(statusString).isEqualTo(dummy.getName());
    }
}
