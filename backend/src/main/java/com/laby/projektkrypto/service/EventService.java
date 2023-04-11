package com.laby.projektkrypto.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laby.projektkrypto.dao.EventRepository;
import com.laby.projektkrypto.dao.EventRepositoryCustom;
import com.laby.projektkrypto.dao.FindEventRequest;
import com.laby.projektkrypto.dto.EventDto;
import com.laby.projektkrypto.entity.Event;
import com.laby.projektkrypto.mappers.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService
{
    private final EventRepository eventRepository;
    private final EventRepositoryCustom eventRepositoryCustom;

    private final EventMapper eventMapper;

    @Transactional
    public void saveEvents(List<EventDto> events)
    {
        log.info("Importing {} events", events.size());
        List<Event> eventsMapped = eventMapper.map(events);
        eventRepository.saveAll(eventsMapped);
    }

    @Transactional(readOnly = true)
    public Iterable<Event> loadAllEvents()
    {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Event> findEvents(FindEventRequest request)
    {
        return eventRepositoryCustom.findEvents(request);
    }
}
