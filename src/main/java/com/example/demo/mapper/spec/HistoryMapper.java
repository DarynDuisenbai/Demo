package com.example.demo.mapper.spec;


import com.example.demo.dto.responce.History;
import com.example.demo.domain.Agreement;

import java.util.List;


public interface HistoryMapper {
    History toHistory(List<Agreement> agreement, String goal);
}
