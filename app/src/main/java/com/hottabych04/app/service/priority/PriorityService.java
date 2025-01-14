package com.hottabych04.app.service.priority;

import com.hottabych04.app.database.entity.Priority;
import com.hottabych04.app.database.repository.PriorityRepository;
import com.hottabych04.app.exception.priority.PriorityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class PriorityService {

    private final PriorityRepository priorityRepository;

    public Priority getPriority(String priority){
        return priorityRepository.findByName(priority)
                .orElseThrow(() -> {
                    log.error("Priority: " + priority + " is not found");
                    return new PriorityNotFoundException(priority);
                });
    }

}
