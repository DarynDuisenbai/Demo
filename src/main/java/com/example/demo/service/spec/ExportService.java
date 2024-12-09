package com.example.demo.service.spec;

import com.example.demo.exception.NoConclusionException;
import com.example.demo.exception.UserNotFoundException;
import com.itextpdf.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public interface ExportService {
    ByteArrayInputStream exportToPdf(String IIN) throws NoConclusionException, DocumentException, IOException, UserNotFoundException;
    ByteArrayInputStream exportToExcel(String IIN);
    ByteArrayInputStream generateConclusionPdf(String registrationNumber) throws NoConclusionException, DocumentException, IOException;

}
