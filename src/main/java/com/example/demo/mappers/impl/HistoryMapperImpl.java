package com.example.demo.mappers.impl;

import com.example.demo.dtos.responces.History;
import com.example.demo.mappers.ConclusionMapper;
import com.example.demo.mappers.HistoryMapper;
import com.example.demo.models.Agreement;
import com.example.demo.models.Conclusion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HistoryMapperImpl implements HistoryMapper {

    @Override
    public History toHistory(List<Agreement> agreements, String goal) {
        History history = new History();
        history.setAgreements(agreements);
        history.setGoal(goal);
        return history;
    }
}
