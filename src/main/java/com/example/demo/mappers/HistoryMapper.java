package com.example.demo.mappers;


import com.example.demo.dtos.responces.History;
import com.example.demo.models.Agreement;
import com.example.demo.models.Conclusion;

import java.util.List;


public interface HistoryMapper {
    History toHistory(List<Agreement> agreement, String goal);
}
