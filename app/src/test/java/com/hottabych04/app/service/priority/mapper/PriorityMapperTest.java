package com.hottabych04.app.service.priority.mapper;

import com.hottabych04.app.database.entity.Priority;
import com.hottabych04.app.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PriorityMapperTest extends IntegrationTestBase {

    @Autowired
    private PriorityMapper priorityMapper;

    @Test
    @DisplayName("Success map priority entity to string")
    public void priorityEntityToString(){
        Priority dummy = new Priority(1, "DUMMY");

        String priorityString = priorityMapper.toPriorityString(dummy);

        assertThat(priorityString).isEqualTo(dummy.getName());
    }
}
