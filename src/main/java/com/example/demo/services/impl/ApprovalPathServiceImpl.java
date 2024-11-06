package com.example.demo.services.impl;

import com.example.demo.models.ApprovalPath;
import com.example.demo.repository.ApprovalPathRepository;
import com.example.demo.services.ApprovalPathService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalPathServiceImpl implements ApprovalPathService {
    private final ApprovalPathRepository approvalPathRepository;

    @Override
    public List<ApprovalPath> getAll() {
        return approvalPathRepository.findAll();
    }
}
