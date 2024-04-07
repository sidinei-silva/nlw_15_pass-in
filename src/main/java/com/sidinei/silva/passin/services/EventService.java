package com.sidinei.silva.passin.services;

import com.sidinei.silva.passin.domain.attendee.Attendee;
import com.sidinei.silva.passin.domain.event.Event;
import com.sidinei.silva.passin.domain.event.exceptions.EventFullException;
import com.sidinei.silva.passin.dto.attendee.AttendeeIdDTO;
import com.sidinei.silva.passin.dto.attendee.AttendeeRequestDTO;
import com.sidinei.silva.passin.dto.event.EventIdDTO;
import com.sidinei.silva.passin.dto.event.EventRequestDTO;
import com.sidinei.silva.passin.dto.event.EventResponseDTO;
import com.sidinei.silva.passin.repositories.EventRepository;
import com.sidinei.silva.passin.domain.event.exceptions.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final AttendeeService attendeeService;

    public EventResponseDTO getEventDetail(String eventId) {
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);
        return new EventResponseDTO(event, attendeeList.size());
    }

    public AttendeeIdDTO registerAttendeeOnEvent(String eventId, AttendeeRequestDTO attendeeRequestDTO) {
        this.attendeeService.verifyAttendeeSubscription(attendeeRequestDTO.email(), eventId);
        Event event = this.getEventById(eventId);
        List<Attendee> attendeeList = this.attendeeService.getAllAttendeesFromEvent(eventId);

        if (event.getMaximumAttendees() <= attendeeList.size()){
            throw  new EventFullException("Event is full");
        }

        Attendee newAttendee = new Attendee();
        newAttendee.setName(attendeeRequestDTO.name());
        newAttendee.setEmail(attendeeRequestDTO.email());
        newAttendee.setEvent(event);
        newAttendee.setCreatedAt(LocalDateTime.now());
        this.attendeeService.registerAttendee(newAttendee);

        return new AttendeeIdDTO(newAttendee.getId());
    }

    public EventIdDTO createEvent(EventRequestDTO eventRequestDTO) {
        Event newEvent = new Event();
        newEvent.setTitle(eventRequestDTO.title());
        newEvent.setDetails(eventRequestDTO.details());
        newEvent.setMaximumAttendees(eventRequestDTO.maximumAttendees());
        newEvent.setSlug(createSlug(eventRequestDTO.title()));
        this.eventRepository.save(newEvent);
        return new EventIdDTO(newEvent.getId());

    }

    private String createSlug(String title) {
        // Decomposição canonical
        // Canonical decomposition.
        String normalized = Normalizer.normalize(title, Normalizer.Form.NFD);

        // Remoção de diacríticos
        // Diacritics removal.
        return normalized.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]", "")
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", "-")
                .toLowerCase();
    }

    private Event getEventById(String eventId) {
        return this.eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
    }

}
