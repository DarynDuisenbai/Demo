package com.example.demo.mapper.impl;

import com.example.demo.dto.responce.History;
import com.example.demo.mapper.spec.HistoryMapper;
import com.example.demo.domain.Agreement;
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
