package com.laby.projektkrypto.soap;

import java.util.List;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.laby.projektkrypto.entity.Event;
import com.laby.projektkrypto.mappers.EventMapper;
import com.laby.projektkrypto.service.EventService;
import localhost.soap.GetEventsRequest;
import localhost.soap.GetEventsResponse;
import lombok.RequiredArgsConstructor;

@Endpoint
@RequiredArgsConstructor
public class Controller
{
    private static final String NAMESPACE_URI = "http://localhost/soap";
    private final EventService eventService;
    private final EventMapper mapper;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getEventsRequest")
    @ResponsePayload
    public GetEventsResponse getCountry(@RequestPayload GetEventsRequest request)
    {
        List<Event> events = eventService.findEvents(mapper.map(request));
        List<localhost.soap.Event> eventsSoap = mapper.mapToSoapResponse(events);
        var getEventsResponse = new GetEventsResponse();
        getEventsResponse.getEvents().addAll(eventsSoap);
        return getEventsResponse;
    }
}
