package com.example.demo.service.spec;

import java.io.ByteArrayInputStream;

public interface ExportService {
    ByteArrayInputStream exportToPdf(String IIN);
    ByteArrayInputStream exportToExcel(String IIN);
}
