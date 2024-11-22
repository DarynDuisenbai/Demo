package com.example.demo.repository.spec;

import com.example.demo.domain.Agreement;

import java.util.List;

public interface FilterAgreementRepo {
    List<Agreement> getAgreementsByIInOfCalled(String IINofCalled);
}
