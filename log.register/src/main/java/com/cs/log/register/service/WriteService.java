package com.cs.log.register.service;

import com.cs.log.register.entity.EventDetail;
import com.cs.log.register.entity.LogFinished;
import com.cs.log.register.entity.LogStarted;
import com.cs.log.register.repository.EventDetailRepository;
import com.cs.log.register.repository.LogFinishedRepository;
import com.cs.log.register.repository.LogStartedRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class WriteService {

    @Autowired
    private LogStartedRepository logStartedRepository;
    @Autowired
    private LogFinishedRepository logFinishedRepository;
    @Autowired
    private EventDetailRepository eventDetailRepository;
    private long counter;
    private long alertCounter;

    public void writeEventDetails() {
       log.info("Started writing event details.");
       Pageable pageable = PageRequest.of(0, 100, Sort.unsorted());
       while (true) {
           Slice<LogStarted> slice = logStartedRepository.findAll(pageable);
           List<LogStarted> list = slice.getContent();
           list.stream().forEach(x -> saveEventDetail(x));
           if (!slice.hasNext()) {
               break;
           }
           pageable = slice.nextPageable();
       }
       log.info("Finished writing event details.");
       log.info("Number stored event details : "+counter);
       log.info("Alerts number : "+alertCounter);
    }

    private void saveEventDetail(LogStarted logStarted) {
        Optional<LogFinished> optionalLogFinished = logFinishedRepository.findById(logStarted.getId());
        if (optionalLogFinished.isPresent()) {
            Long duration = optionalLogFinished.get().getTimestamp() - logStarted.getTimestamp();
            eventDetailRepository.save(EventDetail.builder().
                    id(logStarted.getId())
                    .host(logStarted.getHost())
                    .type(logStarted.getType())
                    .alert(duration > 4 ? true : false)
                    .duration(duration)
                    .build());
            ++counter;
            if (duration > 4) ++alertCounter;
        }
    }
}
