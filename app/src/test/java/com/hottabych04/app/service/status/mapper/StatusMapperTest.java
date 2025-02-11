package com.hottabych04.app.service.status.mapper;

import com.hottabych04.app.database.entity.Status;
import com.hottabych04.app.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class StatusMapperTest extends IntegrationTestBase {

    @Autowired
    private StatusMapper statusMapper;

    @Test
    @DisplayName("Success map status entity to string")
    public void statusEntityToString(){
        Status dummy = new Status(1, "DUMMY");
        String statusString = statusMapper.toStatusString(dummy);

        assertThat(statusString).isEqualTo(dummy.getName());
    }
}
