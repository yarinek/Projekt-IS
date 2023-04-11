package com.laby.projektkrypto.rest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laby.projektkrypto.dto.EventDto;
import com.laby.projektkrypto.mappers.EventMapper;
import com.laby.projektkrypto.service.EventService;
import lombok.RequiredArgsConstructor;

import static java.nio.file.StandardOpenOption.CREATE;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rest/events")
public class EventController
{
    public static final String EXPORT_PATH = "exported_events";
    private final EventService eventService;
    private final EventMapper eventMapper;
    private final ObjectMapper objectMapper;

    @PostMapping("/import")
    public ResponseEntity<String> importEvents(@RequestParam("file") MultipartFile file) throws IOException
    {
        List<EventDto> events;
        try (var inputStream = file.getInputStream()) {
            events = Arrays.asList(objectMapper.readValue(inputStream, EventDto[].class));
        }
        eventService.saveEvents(events);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/export")
    public ResponseEntity<UrlResource> exportEvents() throws IOException
    {
        List<EventDto> events = StreamSupport.stream(eventService.loadAllEvents().spliterator(), false)
                .map(eventMapper::map)
                .toList();

        var path = Paths.get(EXPORT_PATH, "events-" + System.currentTimeMillis() + ".json");
        var json = objectMapper.writeValueAsBytes(events);
        Files.write(path, json, CREATE);
        var resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=events_exported.json")
                .body(resource);
    }
}
