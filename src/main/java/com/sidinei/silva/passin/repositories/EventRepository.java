package com.sidinei.silva.passin.repositories;

import com.sidinei.silva.passin.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String>{
}
