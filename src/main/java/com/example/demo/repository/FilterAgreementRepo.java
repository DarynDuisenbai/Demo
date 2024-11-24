package com.example.demo.repository;

import com.example.demo.domain.Agreement;

import java.util.List;

public interface FilterAgreementRepo {
    List<Agreement> getAgreementsByIInOfCalled(String IINofCalled);
}
