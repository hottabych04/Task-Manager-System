package com.hottabych04.app.service.priority.mapper;

import com.hottabych04.app.database.entity.Priority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PriorityMapperTest {

    private PriorityMapper priorityMapper = new PriorityMapperImpl();

    @Test
    @DisplayName("Success map priority entity to string")
    public void priorityEntityToString(){
        Priority dummy = new Priority(1, "DUMMY");

        String priorityString = priorityMapper.toPriorityString(dummy);

        assertThat(priorityString).isEqualTo(dummy.getName());
    }
}
