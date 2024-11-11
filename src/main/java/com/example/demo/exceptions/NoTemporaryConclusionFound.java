package com.example.demo.exceptions;

import com.example.demo.dtos.requests.EditSavedConclusionRequest;

public class NoTemporaryConclusionFound extends Exception {
    public NoTemporaryConclusionFound() {
    }

    public NoTemporaryConclusionFound(String message) {
        super(message);
    }
}
