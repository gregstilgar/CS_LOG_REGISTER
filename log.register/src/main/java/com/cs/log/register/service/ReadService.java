package com.cs.log.register.service;

import com.cs.log.register.dto.LogDto;
import com.cs.log.register.entity.LogFinished;
import com.cs.log.register.entity.LogStarted;
import com.cs.log.register.repository.LogFinishedRepository;
import com.cs.log.register.repository.LogStartedRepository;
import com.cs.log.register.enums.StateEnum;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ReadService {

    @Autowired
    private LogStartedRepository logStartedRepository;
    @Autowired
    private LogFinishedRepository logFinishedRepository;
    private Gson gson = new Gson();

    public void readLogFile(String absolutePath) {
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            File file = new File(absolutePath);
            log.info("Started uploading log file "+file.getName() +" .");
            Instant start = Instant.now();
            inputStream = new FileInputStream(file);
            sc = new Scanner(inputStream, "UTF-8");
            processScanner(sc);
            Instant finish = Instant.now();
            long timeElapsed = Duration.between(start, finish).toMillis();
            log.info("Finished log file uploading.");
            log.info("Uploading time : "+timeElapsed+" milisec.");
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
    }

    private void processScanner(Scanner sc) throws IOException {
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            parseLineAndSaveDB(line);
        }
        if (sc.ioException() != null) {
            throw sc.ioException();
        }
    }

    private void parseLineAndSaveDB(String line) {
        LogDto logDto = gson.fromJson(line, LogDto.class);
        if (logDto.getState().equals(StateEnum.STARTED.name())) {
            logStartedRepository.save(LogStarted.builder()
                    .id(logDto.getId())
                    .state(logDto.getState())
                    .host(logDto.getHost())
                    .type(logDto.getType())
                    .timestamp(logDto.getTimestamp())
                    .build());
        } else if (logDto.getState().equals(StateEnum.FINISHED.name())) {
            logFinishedRepository.save(LogFinished.builder()
                    .id(logDto.getId())
                    .state(logDto.getState())
                    .host(logDto.getHost())
                    .type(logDto.getType())
                    .timestamp(logDto.getTimestamp())
                    .build());
        }
    }
}
