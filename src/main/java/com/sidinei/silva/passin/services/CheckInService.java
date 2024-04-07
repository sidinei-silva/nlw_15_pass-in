package com.sidinei.silva.passin.services;

import com.sidinei.silva.passin.domain.attendee.Attendee;
import com.sidinei.silva.passin.domain.checkin.CheckIn;
import com.sidinei.silva.passin.domain.checkin.exceptions.CheckInAlreadyExistsException;
import com.sidinei.silva.passin.repositories.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;

    public void registerCheckInAttendee(Attendee attendee) {

        this.verifyCheckInExists(attendee.getId());

        CheckIn checkIn = new CheckIn();
        checkIn.setAttendee(attendee);
        checkIn.setCreatedAt(LocalDateTime.now());

        this.checkInRepository.save(checkIn);
    }


    public void verifyCheckInExists(String attendeeId) {
        Optional<CheckIn> isCheckedIn = this.getCheckIn(attendeeId);
        isCheckedIn.ifPresent(checkIn -> {
            throw new CheckInAlreadyExistsException("Check-in already exists for attendee " + checkIn.getAttendee().getId());
        });
        
    }

    public Optional<CheckIn> getCheckIn(String attendeeId) {
        return this.checkInRepository.findByAttendeeId(attendeeId);
    }
}
