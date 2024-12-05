package com.example.demo.service.impl;

import com.example.demo.domain.Action;
import com.example.demo.repository.spec.ActionRepository;
import com.example.demo.service.spec.ActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {
    private final ActionRepository actionRepository;
    @Override
    public List<String> allActions() {
        return actionRepository.findAll().stream().map(Action::getName).toList();
    }
}
