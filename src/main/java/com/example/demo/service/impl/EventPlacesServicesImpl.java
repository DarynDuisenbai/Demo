package com.example.demo.service.impl;

import com.example.demo.domain.EventPlace;
import com.example.demo.repository.spec.EventPlacesRepository;
import com.example.demo.service.spec.EventPlacesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventPlacesServicesImpl implements EventPlacesService {
    private final EventPlacesRepository eventPlacesRepository;

    @Override
    public List<String> allEventPlaces() {
        return eventPlacesRepository.findAll().stream().map(EventPlace::getName).toList();
    }
}
