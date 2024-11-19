package com.example.demo.services;

import java.io.ByteArrayInputStream;

public interface ExportService {
    ByteArrayInputStream exportToPdf(String IIN);
    ByteArrayInputStream exportToExcel(String IIN);
}
