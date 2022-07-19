package com.cs.log.register.service;

import com.cs.log.register.entity.EventDetail;
import com.cs.log.register.entity.LogFinished;
import com.cs.log.register.entity.LogStarted;
import com.cs.log.register.repository.EventDetailRepository;
import com.cs.log.register.repository.LogFinishedRepository;
import com.cs.log.register.repository.LogStartedRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class WriteServiceTest {

    private final String LOG_STARTED_ID = "123456";
    private final String LOG_FINISHED_ID = "123456";
    private final String LOG_STARTED_STATE = "STARTED";
    private final String LOG_FINISHED_STATE = "FINISHED";
    private final String LOG_STARTED_TYPE = "APPLICATION_LOG";
    private final String LOG_FINISHED_TYPE = "APPLICATION_LOG";
    private final String LOG_STARTED_HOST = "HOST123";
    private final String LOG_FINISHED_HOST = "HOST123";
    private final long LOG_STARTED_TIMESTAMP = 1491377495210l;
    private final long LOG_FINISHED_TIMESTAMP = 1491377495218l;

    @Mock
    private LogStartedRepository logStartedRepository;
    @Mock
    private LogFinishedRepository logFinishedRepository;
    @Mock
    private EventDetailRepository eventDetailRepository;
    @Mock
    private Page<LogStarted> slice;
    @InjectMocks
    private WriteService writeService;

    @BeforeEach
    void beforeTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void writeEventDetailsTest() {
        //given
        List<LogStarted> listStarted = Stream.of(LogStarted.builder().
                id(LOG_STARTED_ID)
                .timestamp(LOG_STARTED_TIMESTAMP)
        .host(LOG_STARTED_HOST)
        .state(LOG_STARTED_STATE)
        .type(LOG_STARTED_TYPE).
                        build()).collect(Collectors.toList());
        Optional<LogFinished> optional = Optional.of(LogFinished.builder().id(LOG_FINISHED_ID)
                .timestamp(LOG_FINISHED_TIMESTAMP)
                .host(LOG_FINISHED_HOST)
                .state(LOG_FINISHED_STATE)
                .type(LOG_FINISHED_TYPE).build());
        //when
        when(logStartedRepository.findAll(any(Pageable.class))).thenReturn(slice);
        when(logFinishedRepository.findById(LOG_STARTED_ID)).thenReturn(optional);
        when(slice.getContent()).thenReturn(listStarted);
        writeService.writeEventDetails();
        ArgumentCaptor<EventDetail> argumentCaptor = ArgumentCaptor.forClass(EventDetail.class);
        //then
        verify(eventDetailRepository, times(1)).save(argumentCaptor.capture());
        Assertions.assertTrue(argumentCaptor.getValue().getId().equals(LOG_STARTED_ID));
        Assertions.assertTrue(argumentCaptor.getValue().getHost().equals(LOG_STARTED_HOST));
        Assertions.assertTrue(argumentCaptor.getValue().getType().equals(LOG_STARTED_TYPE));
        Assertions.assertTrue(argumentCaptor.getValue().getDuration().equals(8l));
        Assertions.assertTrue(argumentCaptor.getValue().getId().equals(LOG_STARTED_ID));
    }

}
