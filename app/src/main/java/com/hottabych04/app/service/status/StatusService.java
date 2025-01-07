package com.hottabych04.app.service.status;

import com.hottabych04.app.database.entity.Status;
import com.hottabych04.app.database.repository.StatusRepository;
import com.hottabych04.app.exception.status.StatusNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class StatusService {

    private final StatusRepository statusRepository;

    public Status getStatus(String status){
        return statusRepository.findByName(status)
                .orElseThrow(() -> {
                    log.error("Status: " + status + " is not found");
                    return new StatusNotFoundException("Status not found", status);
                });
    }

}
