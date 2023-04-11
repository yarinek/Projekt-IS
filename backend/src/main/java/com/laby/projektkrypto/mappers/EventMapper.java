package com.laby.projektkrypto.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.laby.projektkrypto.dao.FindEventRequest;
import com.laby.projektkrypto.dto.EventDto;
import com.laby.projektkrypto.entity.Event;
import localhost.soap.GetEventsRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper
{
    EventDto map(Event event);

    @Mapping(target = "id", ignore = true)
    Event map(EventDto event);

    List<Event> map(List<EventDto> event);

    FindEventRequest map(GetEventsRequest request);

    List<localhost.soap.Event> mapToSoapResponse(List<Event> events);
}
