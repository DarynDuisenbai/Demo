package com.example.demo.service.impl;

import com.example.demo.domain.Status;
import com.example.demo.repository.spec.StatusRepository;
import com.example.demo.service.spec.StatusService;
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
