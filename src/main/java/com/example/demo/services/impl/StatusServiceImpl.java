package com.example.demo.services.impl;

import com.example.demo.models.Status;
import com.example.demo.repository.StatusRepository;
import com.example.demo.services.StatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
    private final StatusRepository statusRepository;

    @Override
    public List<String> allStatuses() {
        return statusRepository.findAll().stream().map(Status::getName).toList();
    }
}
