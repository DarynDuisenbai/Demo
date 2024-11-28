package com.example.demo.service.spec;

import com.example.demo.exception.NoConclusionException;
import com.itextpdf.text.DocumentException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface ExportService {
    ByteArrayInputStream exportToPdf(String RegNumber) throws NoConclusionException, DocumentException, IOException;
    ByteArrayInputStream exportToExcel(String IIN);

}
